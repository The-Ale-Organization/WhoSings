package com.musixmatch.whosings.data.state

import com.musixmatch.whosings.business.error.ErrorHandler
import com.musixmatch.whosings.data.model.UserInfo

// Represents base UI states.
sealed class UiState {
    data class Error(val type: ErrorHandler.UIError): UiState()
    object Loading: UiState()
}

sealed class LoginState : UiState() {
    object LoggedIn: LoginState()
}

sealed class HomeState : UiState() {
    data class GameNotStarted (
        val userInfo: UserInfo
    ): HomeState()
    data class GameFinished (
        val userInfo: UserInfo
    ): HomeState()
}