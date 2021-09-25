package com.musixmatch.whosings.di

import android.content.Context
import androidx.room.Room
import com.musixmatch.whosings.data.storage.room.AppDatabase
import com.musixmatch.whosings.data.storage.room.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    private val databaseName = "whosings-db"

    @Provides
    fun provideChannelDao(appDatabase: AppDatabase): UserDao {
        return appDatabase.userDao()
    }

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(appContext, AppDatabase::class.java, databaseName)
            .fallbackToDestructiveMigration()
            .build()
    }

}