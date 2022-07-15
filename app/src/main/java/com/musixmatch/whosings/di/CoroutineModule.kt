package com.musixmatch.whosings.di

import com.musixmatch.whosings.business.util.DefaultDispatcherProvider
import com.musixmatch.whosings.business.util.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
object CoroutineModule {

    @Provides
    fun provideSampleUseCase(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }

}
//blballaa