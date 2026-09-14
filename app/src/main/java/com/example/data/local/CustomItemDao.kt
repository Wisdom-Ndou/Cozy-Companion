package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CustomItemEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CustomItemDao {
    @Query("SELECT * FROM custom_items")
    fun getAllCustomItems(): Flow<List<CustomItemEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdateItem(item: CustomItemEntity)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(items: List<CustomItemEntity>)

    @Query("UPDATE custom_items SET isEquipped = :isEquipped WHERE id = :id")
    suspend fun setItemEquipped(id: String, isEquipped: Boolean)

    @Query("DELETE FROM custom_items")
    suspend fun clearAll()
}
