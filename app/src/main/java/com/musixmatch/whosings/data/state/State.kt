package com.musixmatch.whosings.data.state

import com.musixmatch.whosings.business.error.ErrorHandler

// Represents base UI states.
sealed class UiState {
    data class Error(val type: ErrorHandler.UIError): UiState()
    object Loading: UiState()
}

sealed class LoginState : UiState() {
    object Success: LoginState()
}