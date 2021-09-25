package com.musixmatch.whosings.data.storage.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntity(
    @PrimaryKey val username: String,
    @ColumnInfo(name = "avatar_url") val avatarUrl: String?,
    @ColumnInfo(name = "recent_scores") val scores: List<ScoreEntity>?,
    @ColumnInfo(name = "best_score") val bestScore: Int?
)