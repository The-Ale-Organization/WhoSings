package com.musixmatch.whosings.ui.viewmodel

import androidx.lifecycle.*
import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.business.usecase.GetSongsUseCase
import com.musixmatch.whosings.business.util.DefaultDispatcherProvider
import com.musixmatch.whosings.business.util.DispatcherProvider
import com.musixmatch.whosings.data.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase,
    private val errorHandler: ErrorHandler,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    // Backing property to avoid state updates from other classes.
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    // The UI observes this LiveData to get its state updates.
    val uiState: LiveData<UiState> = _uiState

    private suspend fun emitState(state: UiState) =
        withContext(dispatchers.main()) {
            _uiState.value = state
        }

}