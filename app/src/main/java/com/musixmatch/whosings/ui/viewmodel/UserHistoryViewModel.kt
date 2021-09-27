package com.musixmatch.whosings.ui.viewmodel

import androidx.lifecycle.*
import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.business.usecase.GetRankingUseCase
import com.musixmatch.whosings.business.usecase.GetRecentGamesUseCase
import com.musixmatch.whosings.business.util.DefaultDispatcherProvider
import com.musixmatch.whosings.business.util.DispatcherProvider
import com.musixmatch.whosings.data.model.UserGameItem
import com.musixmatch.whosings.data.model.UserScoreItem
import com.musixmatch.whosings.data.state.RankingState
import com.musixmatch.whosings.data.state.UiState
import com.musixmatch.whosings.data.state.UserHistoryState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class UserHistoryViewModel @Inject constructor(
    private val getRecentGamesUseCase: GetRecentGamesUseCase,
    private val errorHandler: ErrorHandler,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    // Backing property to avoid state updates from other classes.
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    // The UI observes this LiveData to get its state updates.
    val uiState: LiveData<UiState> = _uiState

    fun getRecentGamesScores() = viewModelScope.launch(dispatchers.io()) {
        try {
            val games = getRecentGamesUseCase.getRecentGames()
            emitState(UserHistoryState.RecentGamesAvailable(
                items = games.map {
                    UserGameItem(
                        score = it.score,
                        day = it.day,
                        month = it.month,
                        year = it.year
                    )
                }
            ))
        } catch (exception: Exception) {
            Timber.e(exception)
            val uiError = errorHandler.handleError(exception)
            emitState(UiState.Error(uiError))
        }
    }

    private suspend fun emitState(state: UiState) =
        withContext(dispatchers.main()) {
            _uiState.value = state
        }

}