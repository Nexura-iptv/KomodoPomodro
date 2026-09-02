package com.example.data.repository

import com.example.data.db.AppDatabase
import com.example.data.db.FavoriteChannelEntity
import com.example.data.db.FocusSessionEntity
import com.example.data.db.StudyTaskEntity
import com.example.data.model.ChannelDataSource
import com.example.data.model.StudyChannel
import kotlinx.coroutines.flow.Flow

class StudyRepository(private val db: AppDatabase) {

    val allSessions: Flow<List<FocusSessionEntity>> = db.focusSessionDao().getAllSessions()
    val totalFocusMinutes: Flow<Int?> = db.focusSessionDao().getTotalFocusMinutes()
    val totalQuestionsSolved: Flow<Int?> = db.focusSessionDao().getTotalQuestionsSolved()

    val allTasks: Flow<List<StudyTaskEntity>> = db.studyTaskDao().getAllTasks()
    val favoriteChannels: Flow<List<FavoriteChannelEntity>> = db.favoriteChannelDao().getFavoriteChannels()

    fun getChannels(): List<StudyChannel> = ChannelDataSource.allChannels

    suspend fun saveSession(session: FocusSessionEntity): Long {
        return db.focusSessionDao().insertSession(session)
    }

    suspend fun deleteSession(id: Long) {
        db.focusSessionDao().deleteSession(id)
    }

    suspend fun addTask(task: StudyTaskEntity): Long {
        return db.studyTaskDao().insertTask(task)
    }

    suspend fun toggleTaskCompleted(id: Long, completed: Boolean) {
        db.studyTaskDao().setTaskCompleted(id, completed)
    }

    suspend fun updateTaskSolvedQuestions(id: Long, solved: Int) {
        db.studyTaskDao().updateSolvedQuestions(id, solved)
    }

    suspend fun deleteTask(id: Long) {
        db.studyTaskDao().deleteTask(id)
    }

    suspend fun toggleChannelFavorite(channel: StudyChannel, isCurrentlyFavorite: Boolean) {
        val entity = FavoriteChannelEntity(
            channelId = channel.id,
            channelName = channel.name,
            handle = channel.handle,
            isFavorite = !isCurrentlyFavorite,
            lastVisitedAt = System.currentTimeMillis()
        )
        db.favoriteChannelDao().insertOrUpdate(entity)
    }

    suspend fun recordChannelVisit(channelId: String, name: String, handle: String) {
        val entity = FavoriteChannelEntity(
            channelId = channelId,
            channelName = name,
            handle = handle,
            isFavorite = false,
            lastVisitedAt = System.currentTimeMillis()
        )
        db.favoriteChannelDao().recordVisit(channelId, System.currentTimeMillis())
    }
}
