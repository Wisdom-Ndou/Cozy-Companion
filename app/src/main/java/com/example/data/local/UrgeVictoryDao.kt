package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.UrgeVictory
import kotlinx.coroutines.flow.Flow

@Dao
interface UrgeVictoryDao {
    @Query("SELECT * FROM urge_victories ORDER BY timestamp DESC")
    fun getAllVictories(): Flow<List<UrgeVictory>>

    @Query("SELECT COUNT(*) FROM urge_victories")
    fun getVictoryCount(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVictory(victory: UrgeVictory): Long

    @Query("DELETE FROM urge_victories")
    suspend fun clearAll()
}
