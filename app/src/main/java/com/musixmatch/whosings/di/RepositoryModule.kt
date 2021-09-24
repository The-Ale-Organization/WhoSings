package com.musixmatch.whosings.di

import com.musixmatch.whosings.data.api.ApiHelper
import com.musixmatch.whosings.data.repository.Repository
import com.musixmatch.whosings.data.repository.RepositoryImpl
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
    fun provideRepository(apiHelper: ApiHelper): Repository {
        return RepositoryImpl(
            apiHelper = apiHelper
        )
    }

}
