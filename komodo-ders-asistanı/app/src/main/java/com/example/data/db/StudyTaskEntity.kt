package com.example.data.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "study_tasks")
data class StudyTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val subjectName: String,
    val targetQuestions: Int = 20,
    val solvedQuestions: Int = 0,
    val estimatedMinutes: Int = 30,
    val isCompleted: Boolean = false,
    val relatedChannelName: String? = null,
    val createdAt: Long = System.currentTimeMillis()
)

@Dao
interface StudyTaskDao {
    @Query("SELECT * FROM study_tasks ORDER BY isCompleted ASC, createdAt DESC")
    fun getAllTasks(): Flow<List<StudyTaskEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: StudyTaskEntity): Long

    @Update
    suspend fun updateTask(task: StudyTaskEntity)

    @Query("UPDATE study_tasks SET isCompleted = :isCompleted WHERE id = :id")
    suspend fun setTaskCompleted(id: Long, isCompleted: Boolean)

    @Query("UPDATE study_tasks SET solvedQuestions = :solved WHERE id = :id")
    suspend fun updateSolvedQuestions(id: Long, solved: Int)

    @Query("DELETE FROM study_tasks WHERE id = :id")
    suspend fun deleteTask(id: Long)
}
