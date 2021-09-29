package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.repository.UserRepository
import org.junit.Test

import org.junit.Assert.*
import org.mockito.Mockito

class GetRecentGamesUseCaseTest {

    private val mockUserRepository = Mockito.mock(UserRepository::class.java)
    private val useCase = GetRecentGamesUseCase(mockUserRepository)

    @Test
    fun getRecentGames() {
    }
}