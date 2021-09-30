package com.musixmatch.whosings.di

import com.musixmatch.whosings.data.api.ApiHelper
import com.musixmatch.whosings.data.repository.*
import com.musixmatch.whosings.data.storage.disk.UserDao
import com.musixmatch.whosings.data.storage.disk.PreferencesManager
import com.musixmatch.whosings.data.storage.ram.VolatileMemoryManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(preferencesManager: PreferencesManager, userDao: UserDao): UserRepository {
        return UserRepositoryImpl(preferencesManager, userDao)
    }

    @Provides
    fun provideMusicRepository(apiHelper: ApiHelper, volatileMemoryManager: VolatileMemoryManager): MusicRepository {
        return MusicRepositoryImpl(
            apiHelper = apiHelper,
            volatileMemoryManager = volatileMemoryManager
        )
    }

}
