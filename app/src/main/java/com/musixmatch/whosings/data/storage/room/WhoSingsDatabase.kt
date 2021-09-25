package com.musixmatch.whosings.data.storage.room

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * The [Room] database for this app.
 */
@Database(
    entities = [
        UserEntity::class,
        ScoreEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao
}
