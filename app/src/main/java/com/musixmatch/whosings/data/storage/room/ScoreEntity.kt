package com.musixmatch.whosings.data.storage.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity
data class ScoreEntity(
    @PrimaryKey val score: Int,
    @ColumnInfo(name = "date") val date: Date,
)