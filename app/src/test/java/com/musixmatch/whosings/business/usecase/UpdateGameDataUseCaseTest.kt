package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.data.repository.MusicRepository
import com.musixmatch.whosings.common.data.repository.UserRepository
import com.musixmatch.whosings.question.business.usecase.UpdateGameDataUseCase
import com.musixmatch.whosings.shared.MockitoHelper
import org.junit.Test

import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito

class UpdateGameDataUseCaseTest {

    private val mockUserRepository = Mockito.mock(UserRepository::class.java)
    private val mockMusicRepository = Mockito.mock(MusicRepository::class.java)
    private val useCase = UpdateGameDataUseCase(mockUserRepository, mockMusicRepository)

    @Test
    fun updateData() {
        Mockito.`when`(mockUserRepository.getEnrolledUserName())
            .thenReturn("Jack")

        useCase.updateData(15)

        Mockito.verify(mockUserRepository).updateUser(
            userName = MockitoHelper.eqObject("Jack"),
            score = eq(15),
            time = anyLong())
    }
}