package com.musixmatch.whosings.data.state

import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.data.model.Question
import com.musixmatch.whosings.data.model.UserInfo
import com.musixmatch.whosings.data.model.UserScoreItem

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

enum class AnswerType {
    Correct,
    Wrong,
    Timeout
}