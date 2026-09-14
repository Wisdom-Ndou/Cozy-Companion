package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "journal_entries")
data class JournalEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val mood: String, // "Great", "Good", "Okay", "Bad", "Very difficult"
    val urgeIntensity: Int = 0, // 0 to 10
    val feelings: String = "",
    val trigger: String = "",
    val alternativeAction: String = "",
    val notes: String = "",
    val isRelapseReflection: Boolean = false,
)
