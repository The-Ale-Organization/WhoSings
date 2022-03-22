package com.musixmatch.whosings.data.state

import com.musixmatch.whosings.common.business.error.ErrorHandler
import com.musixmatch.whosings.common.data.model.presentation.Question
import com.musixmatch.whosings.common.data.model.presentation.RecentGameItem
import com.musixmatch.whosings.common.data.model.presentation.UserInfo
import com.musixmatch.whosings.common.data.model.presentation.UserScoreItem

// Represents base UI states.
sealed class UiState {
    data class Error(val type: ErrorHandler.UIError) : UiState()
    object Loading : UiState()
}

sealed class LoginState : UiState() {
    object LoggedIn : LoginState()
}

sealed class HomeState : UiState() {
    data class UserInfoAvailable(
        val userInfo: UserInfo,
        val currentScore: Int?
    ) : HomeState()
    object Logout: HomeState()
}

sealed class QuestionState : UiState() {
    data class ShowQuestion(
        val question: Question,
        val questionIndex: Int,
        val totalQuestions: Int,
        val currentScore: Int,
        val previousAnswerType: AnswerType?
    ) : QuestionState()

    data class GameFinished(
        val score: Int
    ) : QuestionState()
}

sealed class RankingState : UiState() {
    data class RankAvailable(
        val items: List<UserScoreItem>
    ) : RankingState()
}

sealed class UserHistoryState : UiState() {
    data class RecentGamesAvailable(
        val items: List<RecentGameItem>
    ) : UserHistoryState()
}


sealed class TimerState {
    /**
     * @param remainingTime remaining time in seconds.
     * @param progress remaining time normalized in interval (0..100).
     */
    data class Tick(
        val remainingTime: Int,
        val progress: Int
    ) : TimerState()
    object Timeout : TimerState()
}

enum class AnswerType {
    Correct,
    Wrong,
    Timeout
}