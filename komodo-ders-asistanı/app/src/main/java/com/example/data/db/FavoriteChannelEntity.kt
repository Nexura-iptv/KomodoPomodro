package com.example.data.db

import androidx.room.Dao
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.PrimaryKey
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Entity(tableName = "favorite_channels")
data class FavoriteChannelEntity(
    @PrimaryKey
    val channelId: String,
    val channelName: String,
    val handle: String,
    val isFavorite: Boolean = true,
    val lastVisitedAt: Long = System.currentTimeMillis()
)

@Dao
interface FavoriteChannelDao {
    @Query("SELECT * FROM favorite_channels WHERE isFavorite = 1 ORDER BY lastVisitedAt DESC")
    fun getFavoriteChannels(): Flow<List<FavoriteChannelEntity>>

    @Query("SELECT * FROM favorite_channels ORDER BY lastVisitedAt DESC LIMIT 10")
    fun getRecentlyVisitedChannels(): Flow<List<FavoriteChannelEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(favorite: FavoriteChannelEntity)

    @Query("UPDATE favorite_channels SET isFavorite = :isFav WHERE channelId = :channelId")
    suspend fun setFavorite(channelId: String, isFav: Boolean)

    @Query("UPDATE favorite_channels SET lastVisitedAt = :timestamp WHERE channelId = :channelId")
    suspend fun recordVisit(channelId: String, timestamp: Long)

    @Query("SELECT EXISTS(SELECT 1 FROM favorite_channels WHERE channelId = :channelId AND isFavorite = 1)")
    fun isChannelFavorite(channelId: String): Flow<Boolean>
}
