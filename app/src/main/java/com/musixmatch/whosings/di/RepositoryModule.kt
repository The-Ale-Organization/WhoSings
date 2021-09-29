package com.musixmatch.whosings.di

import com.musixmatch.whosings.data.api.ApiHelper
import com.musixmatch.whosings.data.repository.*
import com.musixmatch.whosings.data.storage.room.UserDao
import com.musixmatch.whosings.data.storage.sharedpref.PreferencesManager
import com.musixmatch.whosings.data.storage.sharedpref.VolatileMemoryManager
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
    fun provideUserRepository(preferencesManager: PreferencesManager, userDao: UserDao): UserRepository {
        return UserRepositoryImpl(preferencesManager, userDao)
    }
    
    @Singleton
    @Provides
    fun provideMusicRepository(apiHelper: ApiHelper, volatileMemoryManager: VolatileMemoryManager): MusicRepository {
        return MusicRepositoryImpl(
            apiHelper = apiHelper,
            volatileMemoryManager = volatileMemoryManager
        )
    }

}
