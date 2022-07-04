package com.musixmatch.whosings.di

import com.musixmatch.whosings.business.util.DefaultDispatcherProvider
import com.musixmatch.whosings.business.util.DispatcherProvider
import com.musixmatch.whosings.business.util.DummyInjectedClassImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import javax.inject.Named

@Module
@InstallIn(ViewModelComponent::class)
object CoroutineModule {

    @Provides
    fun provideSampleUseCase(): DispatcherProvider {
        return DefaultDispatcherProvider()
    }

    @Provides
    @Named("dummyString")
    fun provideDummyString(): String = "provided dummy"

    @Provides
    fun provideDummyInjectedClass(): DummyInjectedClassImpl = DummyInjectedClassImpl()

}
