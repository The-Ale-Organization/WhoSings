package com.musixmatch.whosings.di

import com.musixmatch.whosings.data.repository.UserRepository
import com.musixmatch.whosings.data.repository.UserRepositoryImpl
import com.musixmatch.whosings.data.storage.room.UserDao
import com.musixmatch.whosings.data.storage.sharedpref.PreferencesManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Singleton
    @Provides
    fun provideRepository(preferencesManager: PreferencesManager, userDao: UserDao): UserRepository {
        return UserRepositoryImpl(
            preferencesManager = preferencesManager,
            userDao = userDao
        )
    }

}
