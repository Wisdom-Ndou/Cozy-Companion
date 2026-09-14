package com.example.data.repository

import com.example.data.local.AppDatabase
import com.example.data.model.CustomItem
import com.example.data.model.CustomItemEntity
import com.example.data.model.DefaultCatalog
import com.example.data.model.JournalEntry
import com.example.data.model.UrgeVictory
import com.example.data.preferences.UserPreferencesManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flowOf

class AppRepository(
    database: AppDatabase,
    val preferencesManager: UserPreferencesManager,
) {
    private val journalDao = database.journalDao()
    private val urgeVictoryDao = database.urgeVictoryDao()
    private val customItemDao = database.customItemDao()

    val allJournalEntries: Flow<List<JournalEntry>> = journalDao.getAllEntries()
    val allUrgeVictories: Flow<List<UrgeVictory>> = urgeVictoryDao.getAllVictories()
    @Suppress("unused")
    val journalCount: Flow<Int> = journalDao.getEntryCount()
    @Suppress("unused")
    val victoryCount: Flow<Int> = urgeVictoryDao.getVictoryCount()

    val customItems: Flow<List<CustomItem>> = customItemDao.getAllCustomItems().combine(
        // Combine with default catalog
        flowOf(DefaultCatalog.items),
    ) { dbEntities, catalog ->
        val entityMap = dbEntities.associateBy { it.id }
        catalog.map { defaultItem ->
            val saved = entityMap[defaultItem.id]
            if (saved != null) {
                defaultItem.copy(
                    isOwned = saved.isOwned,
                    isEquipped = saved.isEquipped,
                )
            } else {
                defaultItem
            }
        }
    }

    suspend fun insertJournalEntry(entry: JournalEntry, awardPoints: Boolean = true): Long {
        val id = journalDao.insertEntry(entry)
        if (awardPoints) {
            preferencesManager.addPoints(50)
            preferencesManager.recordEngagementToday()
        }
        return id
    }

    suspend fun deleteJournalEntry(entry: JournalEntry) {
        journalDao.deleteEntry(entry)
    }

    suspend fun insertUrgeVictory(victory: UrgeVictory, points: Int = 50): Long {
        val id = urgeVictoryDao.insertVictory(victory)
        preferencesManager.addPoints(points)
        preferencesManager.incrementExerciseCount()
        preferencesManager.recordEngagementToday()
        return id
    }

    suspend fun initializeCatalogIfEmpty() {
        val defaultEntities = DefaultCatalog.items.map {
            CustomItemEntity(
                id = it.id,
                isOwned = it.isOwned,
                isEquipped = it.isEquipped,
                unlockedAt = if (it.isOwned) System.currentTimeMillis() else 0L,
            )
        }
        customItemDao.insertAll(defaultEntities)
    }

    suspend fun unlockItem(item: CustomItem): Boolean {
        if (item.isOwned) return true
        val spent = preferencesManager.spendPoints(item.cost)
        if (spent) {
            customItemDao.insertOrUpdateItem(
                CustomItemEntity(
                    id = item.id,
                    isOwned = true,
                    isEquipped = true,
                    unlockedAt = System.currentTimeMillis(),
                ),
            )
            return true
        }
        return false
    }

    suspend fun toggleEquipItem(item: CustomItem) {
        if (!item.isOwned) return
        val newEquipped = !item.isEquipped

        // If equipping a mutually exclusive slot (e.g. bed vs armchair if both are furniture),
        // we can allow equipping or un-equipping.
        customItemDao.setItemEquipped(item.id, newEquipped)
    }

    suspend fun clearAllData() {
        journalDao.clearAll()
        urgeVictoryDao.clearAll()
        customItemDao.clearAll()
        preferencesManager.clearAllData()
        initializeCatalogIfEmpty()
    }
}
