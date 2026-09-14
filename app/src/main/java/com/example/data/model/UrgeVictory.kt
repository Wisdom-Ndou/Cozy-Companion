package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "urge_victories")
data class UrgeVictory(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val timestamp: Long = System.currentTimeMillis(),
    val durationMinutes: Int = 5,
    val copingToolUsed: String, // "Breathing Exercise", "5-4-3-2-1 Grounding", "5-Minute Delay", "Companion Taps"
    val notes: String = "",
)
