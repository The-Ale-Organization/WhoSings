package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.exception.UserNotFoundException
import com.musixmatch.whosings.common.data.model.entity.UserEntity
import com.musixmatch.whosings.common.data.model.presentation.UserInfo
import com.musixmatch.whosings.common.data.repository.UserRepository
import com.musixmatch.whosings.home.business.usecase.GetUserInfoUseCase
import com.musixmatch.whosings.shared.MockitoHelper
import junit.framework.Assert.assertEquals
import org.junit.Test

import org.mockito.Mockito

class GetUserInfoUseCaseTest {

    private val mockUserRepository = Mockito.mock(UserRepository::class.java)
    private val useCase = GetUserInfoUseCase(mockUserRepository)

    private val userEntity = UserEntity(
        username = "Jack",
        avatarUrl = null,
        scores = listOf(),
        bestScore = 9
    )

    private val userEntityNoBestScore = UserEntity(
        username = "Jack",
        avatarUrl = null,
        scores = listOf(),
        bestScore = null
    )

    private val users = listOf(
        UserEntity(
            username = "Ale",
            avatarUrl = null,
            scores = listOf(),
            bestScore = 3
        ),
        UserEntity(
            username = "Rob",
            avatarUrl = null,
            scores = listOf(),
            bestScore = 20
        ),
        UserEntity(
            username = "Nic",
            avatarUrl = null,
            scores = listOf(),
            bestScore = null
        ),
        UserEntity(
            username = "Sandra",
            avatarUrl = null,
            scores = listOf(),
            bestScore = 13
        ),
        UserEntity(
            username = "Jack",
            avatarUrl = null,
            scores = listOf(),
            bestScore = 9
        ),
    )

    private val usersNoBestScore = listOf(
        UserEntity(username = "Ale",
            avatarUrl = null,
            scores = listOf(),
            bestScore = null),
        UserEntity(
            username = "Jack",
            avatarUrl = null,
            scores = listOf(),
            bestScore = null
        ),
        UserEntity(username = "Rob",
            avatarUrl = null,
            scores = listOf(),
            bestScore = null)
    )

    @Test
    fun getUser() {
        Mockito.`when`(mockUserRepository.getEnrolledUserName())
            .thenReturn("Jack")
        Mockito.`when`(mockUserRepository.getUserByName(MockitoHelper.anyObject()))
            .thenReturn(userEntity)
        Mockito.`when`(mockUserRepository.getRegisteredUsers())
            .thenReturn(users)

        val userInfo = useCase.getUserInfo()

        val expectedUserInfo = UserInfo(
            username = "Jack",
            bestScore = 9,
            rankingPosition = 3,
            usersWithBestScore = 4,
            avatarUrl = ""
        )

        assertEquals(expectedUserInfo, userInfo)
    }

    @Test
    fun getUser_no_best_score() {
        Mockito.`when`(mockUserRepository.getEnrolledUserName())
            .thenReturn("Jack")
        Mockito.`when`(mockUserRepository.getUserByName(MockitoHelper.anyObject()))
            .thenReturn(userEntityNoBestScore)
        Mockito.`when`(mockUserRepository.getRegisteredUsers())
            .thenReturn(usersNoBestScore)

        val userInfo = useCase.getUserInfo()

        val expectedUserInfo = UserInfo(
            username = "Jack",
            bestScore = null,
            rankingPosition = null,
            usersWithBestScore = 0,
            avatarUrl = ""
        )

        assertEquals(expectedUserInfo, userInfo)
    }

    @Test(expected = UserNotFoundException::class)
    fun no_enrolled_user() {
        Mockito.`when`(mockUserRepository.getEnrolledUserName())
            .thenReturn(null)
        useCase.getUserInfo()
    }

    @Test(expected = UserNotFoundException::class)
    fun no_user_by_name() {
        Mockito.`when`(mockUserRepository.getEnrolledUserName())
            .thenReturn("Jack")
        Mockito.`when`(mockUserRepository.getUserByName(MockitoHelper.anyObject()))
            .thenReturn(null)

        useCase.getUserInfo()
    }
}