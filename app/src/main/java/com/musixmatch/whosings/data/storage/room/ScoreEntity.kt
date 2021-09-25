package com.musixmatch.whosings.data.storage.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScoreEntity(
    @PrimaryKey val score: Int,
    @ColumnInfo(name = "day") val day: String,
    @ColumnInfo(name = "month") val month: String,
    @ColumnInfo(name = "year") val year: String,
)