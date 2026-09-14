package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.model.CompanionType
import com.example.data.model.CustomItem
import com.example.data.model.JournalEntry
import com.example.data.model.UrgeVictory
import com.example.data.preferences.UserPreferences
import com.example.data.preferences.UserPreferencesManager
import com.example.data.repository.AppRepository
import com.example.ui.companion.CompanionEvent
import com.example.ui.companion.CompanionStateMachine
import com.example.ui.companion.CompanionUiState
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val preferencesManager = UserPreferencesManager(application)
    private val repository = AppRepository(database, preferencesManager)

    val userPreferences: StateFlow<UserPreferences> = preferencesManager.userPreferencesFlow
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = UserPreferences(
                companionName = "Barnaby",
                companionType = "BEAR",
                points = 150,
                totalTaps = 0,
                onboardingCompleted = false,
                appLockPin = "",
                appLockEnabled = false,
                notificationsEnabled = true,
                exercisesCompletedCount = 0,
                daysEngagedCount = 1,
                lastEngagedDate = "",
            ),
        )

    val journalEntries: StateFlow<List<JournalEntry>> = repository.allJournalEntries
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val urgeVictories: StateFlow<List<UrgeVictory>> = repository.allUrgeVictories
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val customItems: StateFlow<List<CustomItem>> = repository.customItems
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    val companionStateMachine = CompanionStateMachine(
        scope = viewModelScope,
        onPointsEarned = { points ->
            viewModelScope.launch {
                preferencesManager.addPoints(points)
            }
        },
    ) {
        viewModelScope.launch {
            repository.insertUrgeVictory(
                UrgeVictory(
                    copingToolUsed = "Companion Interaction Taps",
                    durationMinutes = 2,
                    notes = "Stayed with companion during temptation"
                ),
                points = 25
            )
        }
    }

    val companionUiState: StateFlow<CompanionUiState> = companionStateMachine.uiState

    init {
        viewModelScope.launch {
            repository.initializeCatalogIfEmpty()
            preferencesManager.recordEngagementToday()
        }
    }

    fun onCompanionTap() {
        viewModelScope.launch {
            preferencesManager.incrementTaps()
            companionStateMachine.handleEvent(CompanionEvent.Tap)
        }
    }

    fun onStrugglingClicked() {
        companionStateMachine.handleEvent(CompanionEvent.ComfortRequested)
    }

    fun addJournalEntry(
        mood: String,
        urgeIntensity: Int,
        feelings: String,
        trigger: String,
        alternativeAction: String,
        notes: String
    ) {
        viewModelScope.launch {
            repository.insertJournalEntry(
                JournalEntry(
                    mood = mood,
                    urgeIntensity = urgeIntensity,
                    feelings = feelings,
                    trigger = trigger,
                    alternativeAction = alternativeAction,
                    notes = notes,
                    isRelapseReflection = false
                ),
                awardPoints = true
            )
            companionStateMachine.handleEvent(CompanionEvent.ExerciseCompleted)
        }
    }

    fun deleteJournalEntry(entry: JournalEntry) {
        viewModelScope.launch {
            repository.deleteJournalEntry(entry)
        }
    }

    fun recordUrgeVictory(toolName: String, durationMinutes: Int, notes: String, points: Int = 40) {
        viewModelScope.launch {
            repository.insertUrgeVictory(
                UrgeVictory(
                    copingToolUsed = toolName,
                    durationMinutes = durationMinutes,
                    notes = notes
                ),
                points = points
            )
            companionStateMachine.handleEvent(CompanionEvent.ExerciseCompleted)
        }
    }

    fun recordRelapseReflection(
        whatHappened: String,
        feelings: String,
        trigger: String,
        needsNow: String
    ) {
        viewModelScope.launch {
            val combinedNotes = buildString {
                if (whatHappened.isNotBlank()) append("What happened: $whatHappened\n")
                if (needsNow.isNotBlank()) append("What I need right now: $needsNow")
            }
            repository.insertJournalEntry(
                JournalEntry(
                    mood = "Difficult moment",
                    urgeIntensity = 0,
                    feelings = feelings,
                    trigger = trigger,
                    alternativeAction = "Paused and reflected with companion",
                    notes = combinedNotes,
                    isRelapseReflection = true
                ),
                awardPoints = true // User is rewarded for courage to show up and reflect!
            )
            companionStateMachine.handleEvent(CompanionEvent.RelapseReflectionStarted)
        }
    }

    fun unlockItem(item: CustomItem, onResult: (Boolean) -> Unit = {}) {
        viewModelScope.launch {
            val success = repository.unlockItem(item)
            onResult(success)
        }
    }

    fun toggleEquipItem(item: CustomItem) {
        viewModelScope.launch {
            repository.toggleEquipItem(item)
        }
    }

    fun completeOnboarding(name: String, type: CompanionType) {
        viewModelScope.launch {
            preferencesManager.setCompanionName(name)
            preferencesManager.setCompanionType(type.name)
            preferencesManager.setOnboardingCompleted(completed = true)
        }
    }

    fun updateCompanionSettings(name: String, type: CompanionType) {
        viewModelScope.launch {
            preferencesManager.setCompanionName(name)
            preferencesManager.setCompanionType(type.name)
        }
    }

    fun setAppLock(pin: String, enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setAppLock(pin, enabled)
        }
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        viewModelScope.launch {
            preferencesManager.setNotificationsEnabled(enabled)
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }
}
