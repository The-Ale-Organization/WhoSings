package com.musixmatch.whosings.login.presentation

import androidx.lifecycle.*
import com.musixmatch.core.BaseViewModel
import com.musixmatch.whosings.common.business.error.ErrorHandler
import com.musixmatch.whosings.login.business.usecase.LoginUseCase
import com.musixmatch.whosings.common.util.DefaultDispatcherProvider
import com.musixmatch.whosings.common.util.DispatcherProvider
import com.musixmatch.whosings.login.business.usecase.RegistrationUseCase
import com.musixmatch.whosings.common.presentation.navigation.NavigationDispatcher
import com.musixmatch.whosings.common.presentation.navigation.Route
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val registrationUseCase: RegistrationUseCase,
    private val loginUseCase: LoginUseCase,
    private val errorHandler: ErrorHandler,
    private val dispatchers: DispatcherProvider = DefaultDispatcherProvider(),
    private val navigationDispatcher: NavigationDispatcher
) : BaseViewModel<LoginContract.Event, LoginContract.State, LoginContract.Effect>() {

    override fun createInitialState() = LoginContract.State()

    override fun handleEvent(event: LoginContract.Event) {
        when (event) {
            is LoginContract.Event.RegisterClicked -> register(event.username)
            is LoginContract.Event.SignInClicked -> login(event.username)
        }
    }

    private fun register(username: String?) = viewModelScope.launch(dispatchers.io()) {
        try {
            registrationUseCase.registerUser(username)
            navigationDispatcher.navigate(Route.Home)
        } catch (exception: Exception) {
            Timber.e(exception)
            val uiError = errorHandler.handleError(exception)
            setState { copy(isLoading = false, error = uiError)
            }
        }
    }

    private fun login(username: String?) = viewModelScope.launch(dispatchers.io()) {
        try {
            loginUseCase.enrollUser(username)
            Timber.d("Login on dispatcher ${System.identityHashCode(navigationDispatcher)}")
            navigationDispatcher.navigate(Route.Home)
        } catch (exception: Exception) {
            Timber.e(exception)
            val uiError = errorHandler.handleError(exception)
            setState {
                copy(isLoading = false, error = uiError)
            }
        }
    }

}