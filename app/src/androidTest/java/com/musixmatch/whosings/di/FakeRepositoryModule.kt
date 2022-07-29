package com.musixmatch.whosings.di

import com.musixmatch.whosings.data.api.ApiHelper
import com.musixmatch.whosings.data.model.entity.ScoreEntity
import com.musixmatch.whosings.data.model.entity.UserEntity
import com.musixmatch.whosings.data.repository.MusicRepository
import com.musixmatch.whosings.data.repository.MusicRepositoryImpl
import com.musixmatch.whosings.data.repository.UserRepository
import com.musixmatch.whosings.data.storage.ram.VolatileMemoryManager
import dagger.Module
import dagger.Provides
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.testing.TestInstallIn

@Module
@TestInstallIn(
    components = [ViewModelComponent::class],
    replaces = [RepositoryModule::class]
)
class FakeRepositoryModule {

    @Provides
    fun provideUserRepository(): UserRepository = FakeUserRepositoryImpl()

    @Provides
    fun provideMusicRepository(
        apiHelper: ApiHelper,
        volatileMemoryManager: VolatileMemoryManager
    ): MusicRepository {
        return MusicRepositoryImpl(
            apiHelper = apiHelper,
            volatileMemoryManager = volatileMemoryManager
        )
    }

}

class FakeUserRepositoryImpl : UserRepository {

    private val user = UserEntity(
        username = "Mark",
        avatarUrl = null,
        scores = listOf(ScoreEntity(157, 2929L)),
        bestScore = 157
    )

    override fun getRegisteredUsers(): List<UserEntity> = listOf(user)

    override fun isUserRegistered(username: String): Boolean = true

    override fun getUserByName(username: String): UserEntity = user

    override fun registerUser(userName: String) {}

    override fun enrollUser(userName: String) {}

    override fun getEnrolledUserName(): String = "Mark"

    override fun updateUser(userName: String, score: Int, time: Long) {}

}