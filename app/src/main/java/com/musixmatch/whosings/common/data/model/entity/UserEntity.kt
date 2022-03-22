package com.musixmatch.whosings.common.data.model.entity

import androidx.room.*
import com.musixmatch.whosings.common.data.storage.disk.ScoreEntityTypeConverter

@Entity(tableName = "user")
@TypeConverters(ScoreEntityTypeConverter::class)
data class UserEntity(
    @PrimaryKey
    val username: String,
    @ColumnInfo(name = "avatar_url")
    val avatarUrl: String?,
    @ColumnInfo(name = "recent_scores")
    val scores: List<ScoreEntity>?,
    @ColumnInfo(name = "best_score") val bestScore: Int?
)