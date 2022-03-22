package com.musixmatch.whosings.home.presentation

import androidx.lifecycle.*
import com.musixmatch.whosings.common.business.error.ErrorHandler
import com.musixmatch.whosings.business.usecase.ClearSessionUseCase
import com.musixmatch.whosings.business.usecase.GetUserInfoUseCase
import com.musixmatch.whosings.common.util.DefaultDispatcherProvider
import com.musixmatch.whosings.common.util.DispatcherProvider
import com.musixmatch.whosings.data.state.HomeState
import com.musixmatch.whosings.data.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserInfoUseCase: GetUserInfoUseCase,
    private val clearSessionUseCase: ClearSessionUseCase,
    private val errorHandler: ErrorHandler,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    // Backing property to avoid state updates from other classes.
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    // The UI observes this LiveData to get its state updates.
    val uiState: LiveData<UiState> = _uiState

    /**
     * @param currentScore score of the game that just finished. Null if we haven't just finished a game.
     */
    fun retrieveUserInfo(currentScore: Int? = null) = viewModelScope.launch(dispatchers.io()) {
        emitState(UiState.Loading)
        try {
            val userInfo = getUserInfoUseCase.getUserInfo()
            emitState(HomeState.UserInfoAvailable(
                userInfo = userInfo,
                currentScore = currentScore
            ))
        } catch (exception: Exception) {
            Timber.e(exception)
            val uiError = errorHandler.handleError(exception)
            emitState(UiState.Error(uiError))
        }
    }

    fun logout() = viewModelScope.launch(dispatchers.io()) {
        clearSessionUseCase.clearSessionData()
        emitState(HomeState.Logout)
    }

    private suspend fun emitState(state: UiState) =
        withContext(dispatchers.main()) {
            _uiState.value = state
        }

}