package com.musixmatch.whosings.question.presentation

import androidx.lifecycle.*
import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.business.usecase.GetSongsUseCase
import com.musixmatch.whosings.business.usecase.QuestionsCreatorUseCase
import com.musixmatch.whosings.business.usecase.UpdateGameDataUseCase
import com.musixmatch.whosings.business.util.DefaultDispatcherProvider
import com.musixmatch.whosings.business.util.DispatcherProvider
import com.musixmatch.whosings.business.util.QUESTIONS_NUMBER
import com.musixmatch.whosings.data.model.presentation.Question
import com.musixmatch.whosings.data.state.AnswerType
import com.musixmatch.whosings.data.state.QuestionState
import com.musixmatch.whosings.data.state.TimerState
import com.musixmatch.whosings.data.state.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import timber.log.Timber
import javax.inject.Inject

/**
 * This ViewModel is shared between HomeFragment and QuestionFragment, so they can share data
 * without needing to pass a lot of data between fragments.
 * @see "https://developer.android.com/topic/libraries/architecture/viewmodel#sharing"
 */

const val AVAILABLE_SECONDS = 10
const val ONE_SECOND = 1000L

@HiltViewModel
class QuestionViewModel @Inject constructor(
    private val getSongsUseCase: GetSongsUseCase,
    private val questionsCreatorUseCase: QuestionsCreatorUseCase,
    private val updateGameDataUseCase: UpdateGameDataUseCase,
    private val errorHandler: ErrorHandler,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider()
) : ViewModel() {

    // Backing property to avoid state updates from other classes.
    private val _uiState: MutableLiveData<UiState> = MutableLiveData()

    // The UI observes this LiveData to get its state updates.
    val uiState: LiveData<UiState> = _uiState

    private val _timerState: MutableLiveData<TimerState> = MutableLiveData()
    val timerStatus: LiveData<TimerState> = _timerState

    private var countDownJob: Job? = null

    private var questionList = listOf<Question>()
    private var currentQuestionIndex = 0

    // Current score.
    private var score = 0

    /**
     * Generate questions to be used in the quiz.
     */
    fun generateQuestions() = viewModelScope.launch(dispatchers.io()) {
        emitState(UiState.Loading)
        try {
            supervisorScope {
                val songs = getSongsUseCase.getSongs(
                    scope = this
                )
                songs.forEach {
                    Timber.d("${it.title} - lyrics: ${it.lyrics?.take(10)}")
                }
                questionList = questionsCreatorUseCase.createQuestions(QUESTIONS_NUMBER)
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
            }
        } catch (exception: Exception) {
            Timber.e(exception)
            val uiError = errorHandler.handleError(exception)
            emitState(UiState.Error(uiError))
        }
    }

    fun processAnswer(answerIndex: Int?) = viewModelScope.launch(dispatchers.io()) {
        stopCountDown()

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

        if (currentQuestionIndex < questionList.size - 1) {
            // Show the next question.
            currentQuestionIndex++
            emitState(
                QuestionState.ShowQuestion(
                    question = questionList[currentQuestionIndex],
                    questionIndex = currentQuestionIndex,
                    totalQuestions = questionList.size,
                    currentScore = score,
                    previousAnswerType = answerType
                )
            )
        } else {
            // No more questions to show. Finish game.
            val finalScore = score
            currentQuestionIndex = 0
            score = 0
            questionList = listOf()
            // Update game history data.
            updateGameDataUseCase.updateData(finalScore)
            emitState(QuestionState.GameFinished(finalScore))
        }
    }

    fun startCountDown() {
        if (countDownJob == null) {
            countDownJob = viewModelScope.launch {
                Timber.d("TIMER - Start countdown")
                (dispatchers.io()) {
                    var remainingTime = AVAILABLE_SECONDS
                    while (remainingTime > 0) {
                        Timber.d("TIMER - Remaining time: $remainingTime")
                        val progress = remainingTime * 100 / AVAILABLE_SECONDS
                        emitTimerState(
                            TimerState.Tick(
                                progress = progress,
                                remainingTime = remainingTime
                            )
                        )
                        remainingTime--
                        delay(ONE_SECOND)
                    }
                    Timber.d("TIMER - timeout reached")
                    // Timeout reached.
                    emitTimerState(TimerState.Timeout)
                }
            }
        }
    }

    private fun stopCountDown() {
        // Stop countdown.
        Timber.d("TIMER - Cancel countdown")
        countDownJob?.cancel()
        countDownJob = null
    }

    private suspend fun emitState(state: UiState) =
        withContext(dispatchers.main()) {
            _uiState.value = state
        }

    private suspend fun emitTimerState(state: TimerState) =
        withContext(dispatchers.main()) {
            _timerState.value = state
        }

}