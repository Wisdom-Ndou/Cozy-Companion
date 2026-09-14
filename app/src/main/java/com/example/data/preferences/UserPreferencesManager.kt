package com.example.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

data class UserPreferences(
    val companionName: String,
    val companionType: String,
    val points: Int,
    val totalTaps: Int,
    val onboardingCompleted: Boolean,
    val appLockPin: String,
    val appLockEnabled: Boolean,
    val notificationsEnabled: Boolean,
    val exercisesCompletedCount: Int,
    val daysEngagedCount: Int,
    val lastEngagedDate: String,
)

class UserPreferencesManager(private val context: Context) {

    private object Keys {
        val COMPANION_NAME = stringPreferencesKey("companion_name")
        val COMPANION_TYPE = stringPreferencesKey("companion_type")
        val POINTS = intPreferencesKey("points")
        val TOTAL_TAPS = intPreferencesKey("total_taps")
        val ONBOARDING_COMPLETED = booleanPreferencesKey("onboarding_completed")
        val APP_LOCK_PIN = stringPreferencesKey("app_lock_pin")
        val APP_LOCK_ENABLED = booleanPreferencesKey("app_lock_enabled")
        val NOTIFICATIONS_ENABLED = booleanPreferencesKey("notifications_enabled")
        val EXERCISES_COMPLETED_COUNT = intPreferencesKey("exercises_completed_count")
        val DAYS_ENGAGED_COUNT = intPreferencesKey("days_engaged_count")
        val LAST_ENGAGED_DATE = stringPreferencesKey("last_engaged_date")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data.map { prefs ->
        UserPreferences(
            companionName = prefs[Keys.COMPANION_NAME] ?: "Barnaby",
            companionType = prefs[Keys.COMPANION_TYPE] ?: "BEAR",
            points = prefs[Keys.POINTS] ?: 150,
            totalTaps = prefs[Keys.TOTAL_TAPS] ?: 0,
            onboardingCompleted = prefs[Keys.ONBOARDING_COMPLETED] ?: false,
            appLockPin = prefs[Keys.APP_LOCK_PIN] ?: "",
            appLockEnabled = prefs[Keys.APP_LOCK_ENABLED] ?: false,
            notificationsEnabled = prefs[Keys.NOTIFICATIONS_ENABLED] ?: true,
            exercisesCompletedCount = prefs[Keys.EXERCISES_COMPLETED_COUNT] ?: 0,
            daysEngagedCount = prefs[Keys.DAYS_ENGAGED_COUNT] ?: 1,
            lastEngagedDate = prefs[Keys.LAST_ENGAGED_DATE] ?: "",
        )
    }

    suspend fun setCompanionName(name: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.COMPANION_NAME] = name
        }
    }

    suspend fun setCompanionType(type: String) {
        context.dataStore.edit { prefs ->
            prefs[Keys.COMPANION_TYPE] = type
        }
    }

    suspend fun addPoints(amount: Int) {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.POINTS] ?: 150
            prefs[Keys.POINTS] = (current + amount).coerceAtLeast(0)
        }
    }

    suspend fun spendPoints(amount: Int): Boolean {
        var success = false
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.POINTS] ?: 150
            if (current >= amount) {
                prefs[Keys.POINTS] = current - amount
                success = true
            }
        }
        return success
    }

    suspend fun incrementTaps() {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.TOTAL_TAPS] ?: 0
            prefs[Keys.TOTAL_TAPS] = current + 1
        }
    }

    suspend fun incrementExerciseCount() {
        context.dataStore.edit { prefs ->
            val current = prefs[Keys.EXERCISES_COMPLETED_COUNT] ?: 0
            prefs[Keys.EXERCISES_COMPLETED_COUNT] = current + 1
        }
    }

    suspend fun recordEngagementToday() {
        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        context.dataStore.edit { prefs ->
            val lastDate = prefs[Keys.LAST_ENGAGED_DATE] ?: ""
            if (lastDate != today) {
                val currentDays = prefs[Keys.DAYS_ENGAGED_COUNT] ?: 1
                prefs[Keys.DAYS_ENGAGED_COUNT] = currentDays + 1
                prefs[Keys.LAST_ENGAGED_DATE] = today
            }
        }
    }

    suspend fun setOnboardingCompleted(completed: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.ONBOARDING_COMPLETED] = completed
        }
    }

    suspend fun setAppLock(pin: String, enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.APP_LOCK_PIN] = pin
            prefs[Keys.APP_LOCK_ENABLED] = enabled
        }
    }

    suspend fun setNotificationsEnabled(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[Keys.NOTIFICATIONS_ENABLED] = enabled
        }
    }

    suspend fun clearAllData() {
        context.dataStore.edit { prefs ->
            prefs.clear()
        }
    }
}
