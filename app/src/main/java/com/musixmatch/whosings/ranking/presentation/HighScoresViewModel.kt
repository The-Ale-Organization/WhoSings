package com.musixmatch.whosings.ranking.presentation

import androidx.lifecycle.*
import com.musixmatch.whosings.common.business.error.ErrorHandler
import com.musixmatch.whosings.business.usecase.GetRankingUseCase
import com.musixmatch.whosings.common.util.DefaultDispatcherProvider
import com.musixmatch.whosings.common.util.DispatcherProvider
import com.musixmatch.whosings.common.data.model.presentation.UserScoreItem
import com.musixmatch.whosings.data.state.RankingState
import com.musixmatch.whosings.data.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HighScoresViewModel @Inject constructor(
    private val getRankingUseCase: GetRankingUseCase,
    private val errorHandler: ErrorHandler,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    // Backing property to avoid state updates from other classes.
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    // The UI observes this LiveData to get its state updates.
    val uiState: LiveData<UiState> = _uiState

    fun getRanking() = viewModelScope.launch(dispatchers.io()) {
        try {
            val sortedUsers = getRankingUseCase.getRanking()
            emitState(RankingState.RankAvailable(
                items = sortedUsers.map {
                    UserScoreItem(
                        userName = it.first,
                        avatarUrl = null,
                        score = it.second
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