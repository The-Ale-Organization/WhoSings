package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.exception.UserNotFoundException
import com.musixmatch.whosings.common.data.model.entity.ScoreEntity
import com.musixmatch.whosings.common.data.model.entity.UserEntity
import com.musixmatch.whosings.common.data.repository.UserRepository
import com.musixmatch.whosings.history.business.usecase.GetRecentGamesUseCase
import com.musixmatch.whosings.shared.MockitoHelper
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito

class GetRecentGamesUseCaseTest {

    private val mockUserRepository = Mockito.mock(UserRepository::class.java)
    private val useCase = GetRecentGamesUseCase(mockUserRepository)

    private val scores = listOf(
        ScoreEntity(
            score = 20,
            time = 49900090
        ),
        ScoreEntity(
            score = 20,
            time = 340
        ),
        ScoreEntity(
            score = 20,
            time = 303030330
        )
    )

    private val sortedScores  = listOf(
        ScoreEntity(
            score = 20,
            time = 303030330
        ),
        ScoreEntity(
            score = 20,
            time = 49900090
        ),
        ScoreEntity(
            score = 20,
            time = 340
        )
    )

    @Test
    fun getRecentGames() {
        Mockito.`when`(mockUserRepository.getEnrolledUserName())
            .thenReturn("Jack")
        Mockito.`when`(mockUserRepository.getUserByName(MockitoHelper.anyObject()))
            .thenReturn(UserEntity(
                username = "Jack",
                avatarUrl = null,
                scores = scores,
                bestScore = null
            ))

       val result = useCase.getRecentGames()
        assertEquals(sortedScores, result)
    }

    @Test(expected = UserNotFoundException::class)
    fun no_enrolled_user() {
        Mockito.`when`(mockUserRepository.getEnrolledUserName())
            .thenReturn(null)
        useCase.getRecentGames()
    }

    @Test(expected = UserNotFoundException::class)
    fun no_user_by_name() {
        Mockito.`when`(mockUserRepository.getEnrolledUserName())
            .thenReturn("Jack")
        Mockito.`when`(mockUserRepository.getUserByName(MockitoHelper.anyObject()))
            .thenReturn(null)

        useCase.getRecentGames()
    }
}