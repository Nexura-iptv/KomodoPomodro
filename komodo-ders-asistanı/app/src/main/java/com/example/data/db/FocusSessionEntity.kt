package com.example.data.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "focus_sessions")
data class FocusSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val subjectName: String,
    val durationMinutes: Int,
    val modeType: String, // POMODORO, SHORT_BREAK, LONG_BREAK, DEEP_FOCUS, EXAM_SIM, STOPWATCH
    val questionsSolved: Int = 0,
    val notes: String = "",
    val timestamp: Long = System.currentTimeMillis()
)

@Dao
interface FocusSessionDao {
    @Query("SELECT * FROM focus_sessions ORDER BY timestamp DESC")
    fun getAllSessions(): Flow<List<FocusSessionEntity>>

    @Query("SELECT * FROM focus_sessions WHERE timestamp >= :sinceTimestamp ORDER BY timestamp DESC")
    fun getSessionsSince(sinceTimestamp: Long): Flow<List<FocusSessionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: FocusSessionEntity): Long

    @Query("SELECT SUM(durationMinutes) FROM focus_sessions WHERE modeType != 'SHORT_BREAK' AND modeType != 'LONG_BREAK'")
    fun getTotalFocusMinutes(): Flow<Int?>

    @Query("SELECT SUM(questionsSolved) FROM focus_sessions")
    fun getTotalQuestionsSolved(): Flow<Int?>

    @Query("DELETE FROM focus_sessions WHERE id = :id")
    suspend fun deleteSession(id: Long)
}
