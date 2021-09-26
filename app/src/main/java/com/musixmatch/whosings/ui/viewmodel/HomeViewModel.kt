package com.musixmatch.whosings.ui.viewmodel

import androidx.lifecycle.*
import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.business.usecase.GetSongsUseCase
import com.musixmatch.whosings.business.usecase.GetUserInfoUseCase
import com.musixmatch.whosings.business.usecase.QuestionsCreatorUseCase
import com.musixmatch.whosings.business.util.DefaultDispatcherProvider
import com.musixmatch.whosings.business.util.DispatcherProvider
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
    private val getSongsUseCase: GetSongsUseCase,
    private val questionsCreatorUseCase: QuestionsCreatorUseCase,
    private val errorHandler: ErrorHandler,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    // Backing property to avoid state updates from other classes.
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    // The UI observes this LiveData to get its state updates.
    val uiState: LiveData<UiState> = _uiState

    fun retrieveUserInfo() = viewModelScope.launch(dispatchers.io()) {
        emitState(UiState.Loading)
        try {
            val userInfo = getUserInfoUseCase.getUser()
            emitState(HomeState.GameNotStarted(
                userInfo = userInfo
            ))
        } catch (exception: Exception) {
            Timber.e(exception)
            val uiError = errorHandler.handleError(exception)
            emitState(UiState.Error(uiError))
        }
    }

    fun generateQuestions() = viewModelScope.launch(dispatchers.io()) {
        emitState(UiState.Loading)
        try {
            val songs = getSongsUseCase.getSongs(
                scope = this
            )
            songs.forEach {
                Timber.d("${it.title} - lyrics: ${it.lyrics?.take(10)}")
            }

            val questions = questionsCreatorUseCase.createQuestions()
            questions.forEach {
                Timber.d("Question: Who sings '${it.lyricsLine}'?")
                Timber.d("Answer 1 (${it.correctAnswerIndex == 0}): ${it.answers[0]}")
                Timber.d("Answer 2 (${it.correctAnswerIndex == 1}): ${it.answers[1]}")
                Timber.d("Answer 3 (${it.correctAnswerIndex == 2}): ${it.answers[2]}")
            }

            emitState(HomeState.StartGame(
                questionList = questions
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