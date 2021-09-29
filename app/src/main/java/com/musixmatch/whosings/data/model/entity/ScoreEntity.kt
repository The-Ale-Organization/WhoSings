package com.musixmatch.whosings.data.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ScoreEntity(
    @PrimaryKey val score: Int,
    @ColumnInfo (name = "time") val time: Long
)