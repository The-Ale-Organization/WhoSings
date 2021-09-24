package com.musixmatch.whosings.ui.viewmodel

import androidx.lifecycle.*
import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.business.util.DefaultDispatcherProvider
import com.musixmatch.whosings.business.util.DispatcherProvider
import com.musixmatch.whosings.business.usecase.SampleUseCase
import com.musixmatch.whosings.data.state.LoginState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val sampleUseCase: SampleUseCase,
    private val errorHandler: ErrorHandler,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    // Backing property to avoid state updates from other classes.
    private val _uiState: MutableLiveData<LoginState> = MutableLiveData()
    // The UI observes this LiveData to get its state updates.
    val uiState: LiveData<LoginState> = _uiState

    fun getData() = viewModelScope.launch(dispatchers.io()) {
        emitState(LoginState.Loading)

        try {

        } catch (exception: Exception) {
            Timber.e(exception)
            val uiError = errorHandler.handleError(exception)
            emitState(LoginState.Error(uiError))
        }
    }

    private suspend fun emitState(state: LoginState) =
        withContext(dispatchers.main()) {
            _uiState.value = state
        }

}