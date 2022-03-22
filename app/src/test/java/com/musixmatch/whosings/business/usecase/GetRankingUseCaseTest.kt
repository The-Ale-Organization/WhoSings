package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.data.model.entity.UserEntity
import com.musixmatch.whosings.common.data.repository.UserRepository
import com.musixmatch.whosings.ranking.business.usecase.GetRankingUseCase
import org.junit.Test
import org.junit.Assert.*
import org.mockito.Mockito

class GetRankingUseCaseTest {

    private val mockUserRepository = Mockito.mock(UserRepository::class.java)
    private val getRankingUseCase = GetRankingUseCase(mockUserRepository)

    private val users = listOf(
        UserEntity(username = "Ale",
            avatarUrl = null,
            scores = listOf(),
            bestScore = 3),
        UserEntity(username = "Rob",
            avatarUrl = null,
            scores = listOf(),
            bestScore = 20),
        UserEntity(username = "Nic",
            avatarUrl = null,
            scores = listOf(),
            bestScore = null),
        UserEntity(username = "Sandra",
            avatarUrl = null,
            scores = listOf(),
            bestScore = 13),
        UserEntity(username = "Jack",
            avatarUrl = null,
            scores = listOf(),
            bestScore = 9),
    )

    private val usersNoBestScore = listOf(
        UserEntity(username = "Ale",
            avatarUrl = null,
            scores = listOf(),
            bestScore = null),
        UserEntity(username = "Rob",
            avatarUrl = null,
            scores = listOf(),
            bestScore = null)
    )

    @Test
    fun getRanking() {
        Mockito.`when`(mockUserRepository.getRegisteredUsers())
            .thenReturn(users)

        val expectedRanking = listOf(
            Pair("Rob", 20),
            Pair("Sandra", 13),
            Pair("Jack", 9),
            Pair("Ale", 3)
        )
        val ranking = getRankingUseCase.getRanking()

        assertEquals(expectedRanking, ranking)
    }

    @Test
    fun getRanking_empty_list() {
        Mockito.`when`(mockUserRepository.getRegisteredUsers())
            .thenReturn(listOf())

        val ranking = getRankingUseCase.getRanking()

        assertEquals(listOf<Pair<String, Int>>(), ranking)
    }

    @Test
    fun getRanking_no_best_score() {
        Mockito.`when`(mockUserRepository.getRegisteredUsers())
            .thenReturn(usersNoBestScore)

        val ranking = getRankingUseCase.getRanking()

        assertEquals(listOf<Pair<String, Int>>(), ranking)
    }
}