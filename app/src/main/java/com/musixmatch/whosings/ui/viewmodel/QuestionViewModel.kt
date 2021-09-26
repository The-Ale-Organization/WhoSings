package com.musixmatch.whosings.ui.viewmodel

import androidx.lifecycle.*
import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.business.usecase.GetSongsUseCase
import com.musixmatch.whosings.business.usecase.QuestionsCreatorUseCase
import com.musixmatch.whosings.business.util.DefaultDispatcherProvider
import com.musixmatch.whosings.business.util.DispatcherProvider
import com.musixmatch.whosings.data.model.Question
import com.musixmatch.whosings.data.state.AnswerType
import com.musixmatch.whosings.data.state.QuestionState
import com.musixmatch.whosings.data.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * This ViewModel is shared between HomeFragment and QuestionFragment, so they can share data
 * without needing to pass a lot of data between fragments.
 * @see "https://developer.android.com/topic/libraries/architecture/viewmodel#sharing"
 */

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase,
    private val questionsCreatorUseCase: QuestionsCreatorUseCase,
    private val errorHandler: ErrorHandler,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    // Backing property to avoid state updates from other classes.
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()
    // The UI observes this LiveData to get its state updates.
    val uiState: LiveData<UiState> = _uiState

    var questionList = listOf<Question>()
    var currentQuestionIndex = 0

    // Current score.
    private var score = 0

    fun generateQuestions() = viewModelScope.launch(dispatchers.io()) {
        emitState(UiState.Loading)
        try {
            val songs = getSongsUseCase.getSongs(
                scope = this
            )
            songs.forEach {
                Timber.d("${it.title} - lyrics: ${it.lyrics?.take(10)}")
            }

            questionList = questionsCreatorUseCase.createQuestions()
            questionList.forEach {
                Timber.d("Question: Who sings '${it.lyricsLine}'?")
                Timber.d("Answer 1 (${it.correctAnswerIndex == 0}): ${it.answers[0]}")
                Timber.d("Answer 2 (${it.correctAnswerIndex == 1}): ${it.answers[1]}")
                Timber.d("Answer 3 (${it.correctAnswerIndex == 2}): ${it.answers[2]}")
            }

            emitState(
                QuestionState.ShowQuestion(
                    question = questionList[currentQuestionIndex],
                    questionIndex = currentQuestionIndex,
                    totalQuestions = questionList.size,
                    currentScore = score,
                    previousAnswerType = null
                )
            )
        } catch (exception: Exception) {
            Timber.e(exception)
            val uiError = errorHandler.handleError(exception)
            emitState(UiState.Error(uiError))
        }
    }

    fun processAnswer(answerIndex: Int?) = viewModelScope.launch(dispatchers.io()) {
        val answerType = when (answerIndex) {
            null -> {
                // No answer given (timeout reached).
                AnswerType.Timeout
            }
            questionList[currentQuestionIndex].correctAnswerIndex -> {
                // Selected answer is correct.
                score++
                AnswerType.Correct
            }
            else -> {
                // Selected answer is wrong.
                AnswerType.Wrong
            }
        }

        if (currentQuestionIndex < questionList.size -1) {
            // Show the next question.
            currentQuestionIndex++
            emitState(QuestionState.ShowQuestion(
                question = questionList[currentQuestionIndex],
                questionIndex = currentQuestionIndex,
                totalQuestions = questionList.size,
                currentScore = score,
                previousAnswerType = answerType
            ))
        } else {
            // No more questions to show. Finish game.
            emitState(QuestionState.EndGame)
        }
    }

    private suspend fun emitState(state: UiState) =
        withContext(dispatchers.main()) {
            _uiState.value = state
        }

}