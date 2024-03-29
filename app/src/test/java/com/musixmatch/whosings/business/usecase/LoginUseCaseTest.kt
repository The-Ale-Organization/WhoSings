package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.exception.EmptyUserException
import com.musixmatch.whosings.data.exception.UserNotFoundException
import com.musixmatch.whosings.data.repository.UserRepository
import com.musixmatch.whosings.shared.MockitoHelper
import org.junit.Test

import org.junit.Assert.*
import org.mockito.ArgumentMatchers
import org.mockito.Mockito

class LoginUseCaseTest {

    private val mockUserRepository = Mockito.mock(UserRepository::class.java)
    private val useCase = LoginUseCase(mockUserRepository)

    @Test(expected = EmptyUserException::class)
    fun enrollUser_empty() {
        useCase.enrollUser(null)
    }

    @Test(expected = UserNotFoundException::class)
    fun enrollUser_not_found() {
        Mockito.`when`(mockUserRepository.isUserRegistered(MockitoHelper.anyObject()))
            .thenReturn(false)
        useCase.enrollUser("Jack")
    }

    @Test
    fun enrollUser() {
        Mockito.`when`(mockUserRepository.isUserRegistered(MockitoHelper.anyObject()))
            .thenReturn(true)
        useCase.enrollUser("Jack")
        Mockito.verify(mockUserRepository).enrollUser("Jack")
    }
}