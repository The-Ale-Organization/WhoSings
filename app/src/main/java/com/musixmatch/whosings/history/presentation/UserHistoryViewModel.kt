package com.musixmatch.whosings.history.presentation

import androidx.lifecycle.*
import com.musixmatch.whosings.common.business.error.ErrorHandler
import com.musixmatch.whosings.history.business.usecase.GetRecentGamesUseCase
import com.musixmatch.whosings.common.util.DateConverter
import com.musixmatch.whosings.common.util.DefaultDispatcherProvider
import com.musixmatch.whosings.common.util.DispatcherProvider
import com.musixmatch.whosings.common.data.model.presentation.RecentGameItem
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
    private val dateConverter: DateConverter,
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
                    RecentGameItem(
                        score = it.score,
                        time = dateConverter.formatTime(it.time)
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