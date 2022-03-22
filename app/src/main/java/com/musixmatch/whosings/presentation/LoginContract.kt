package com.musixmatch.whosings.presentation

import com.musixmatch.core.UiEffect
import com.musixmatch.core.UiEvent
import com.musixmatch.core.UiState
import com.musixmatch.whosings.business.error.ErrorHandler

class LoginContract {

    // Events that user performed
    sealed class Event : UiEvent {
        data class SignInClicked(val username: String?) : Event()
        data class RegisterClicked(val username: String?) : Event()
    }

    // UI View States
    data class State(
        val isLoading: Boolean = false,
        val error: ErrorHandler.UIError? = null
    ) : UiState

    // Side effects
    sealed class Effect : UiEffect {
        object ShowToast : Effect()
    }

}