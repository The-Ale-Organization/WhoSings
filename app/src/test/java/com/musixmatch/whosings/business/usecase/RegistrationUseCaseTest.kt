package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.exception.AlreadyRegisteredUserException
import com.musixmatch.whosings.common.exception.EmptyUserException
import com.musixmatch.whosings.common.data.repository.UserRepository
import com.musixmatch.whosings.login.business.usecase.RegistrationUseCase
import com.musixmatch.whosings.shared.MockitoHelper
import org.junit.Test

import org.mockito.Mockito

class RegistrationUseCaseTest {

    private val mockUserRepository = Mockito.mock(UserRepository::class.java)
    private val useCase = RegistrationUseCase(mockUserRepository)

    @Test(expected = EmptyUserException::class)
    fun registerUser_empty() {
        useCase.registerUser(null)
    }

    @Test(expected = AlreadyRegisteredUserException::class)
    fun registerUser_already_registered() {
        Mockito.`when`(mockUserRepository.isUserRegistered(MockitoHelper.anyObject()))
            .thenReturn(true)
        useCase.registerUser("Jack")
    }

    @Test
    fun registerUser() {
        Mockito.`when`(mockUserRepository.isUserRegistered(MockitoHelper.anyObject()))
            .thenReturn(false)
        useCase.registerUser("Jack")
        Mockito.verify(mockUserRepository).registerUser("Jack")
        Mockito.verify(mockUserRepository).enrollUser("Jack")
    }

}