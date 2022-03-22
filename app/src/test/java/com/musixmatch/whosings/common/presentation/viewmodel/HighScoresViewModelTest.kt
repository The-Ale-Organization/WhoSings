package com.musixmatch.whosings.common.presentation.viewmodel

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.musixmatch.whosings.common.business.error.ErrorHandler
import com.musixmatch.whosings.business.usecase.GetRankingUseCase
import com.musixmatch.whosings.common.data.model.presentation.UserScoreItem
import com.musixmatch.whosings.data.state.RankingState
import com.musixmatch.whosings.data.state.UiState
import com.musixmatch.whosings.ranking.presentation.HighScoresViewModel
import com.musixmatch.whosings.shared.CoroutineTestRule
import com.musixmatch.whosings.shared.MockitoHelper
import com.musixmatch.whosings.shared.getOrAwaitValue
import com.musixmatch.whosings.shared.observeForTesting
import junit.framework.Assert.assertEquals
import org.junit.Test
import org.junit.Rule
import org.junit.rules.TestRule
import org.mockito.Mockito
import java.lang.RuntimeException

class HighScoresViewModelTest {

    @get:Rule var rule: TestRule = InstantTaskExecutorRule()

    // Set the coroutines dispatcher for unit testing
    @get:Rule
    var coroutinesTestRule = CoroutineTestRule()

    val mockRankingUseCase = Mockito.mock(GetRankingUseCase::class.java)
    val mockErrorHandler = Mockito.mock(ErrorHandler::class.java)
    val viewModel = HighScoresViewModel(
        getRankingUseCase = mockRankingUseCase,
        errorHandler = mockErrorHandler
    )

    val ranking = listOf(
        Pair("Rob", 20),
        Pair("Sandra", 13),
        Pair("Jack", 9),
        Pair("Ale", 3)
    )

    val expectedItems = listOf(
        UserScoreItem(
            userName = "Rob",
            avatarUrl = null,
            score = 20
        ),
        UserScoreItem(
            userName = "Sandra",
            avatarUrl = null,
            score = 13
        ),
        UserScoreItem(
            userName = "Jack",
            avatarUrl = null,
            score = 9
        ),
        UserScoreItem(
            userName = "Ale",
            avatarUrl = null,
            score = 3
        )
    )

    @Test
    fun getRanking_ok() {
        Mockito.`when`(mockRankingUseCase.getRanking())
            .thenReturn(ranking)

        viewModel.uiState.observeForTesting{}
        viewModel.getRanking()

        assertEquals((RankingState.RankAvailable(
            items = expectedItems)
        ), viewModel.uiState.getOrAwaitValue())
    }

    @Test
    fun getRanking_error() {
        Mockito.`when`(mockRankingUseCase.getRanking())
            .thenThrow(RuntimeException::class.java)
        Mockito.`when`(mockErrorHandler.handleError(MockitoHelper.anyObject()))
            .thenReturn(ErrorHandler.UIError.GenericError)

        viewModel.uiState.observeForTesting{}
        viewModel.getRanking()

        assertEquals(UiState.Error(ErrorHandler.UIError.GenericError), viewModel.uiState.getOrAwaitValue())
    }

}