package com.musixmatch.whosings.common.business.error

import com.musixmatch.whosings.common.exception.AlreadyRegisteredUserException
import com.musixmatch.whosings.common.exception.EmptyUserException
import com.musixmatch.whosings.common.exception.UserNotFoundException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * A simple error handler, which can be expanded to handle more specific errors.
 *
 * Maps business logic errors to UI errors.
 *
 */
class ErrorHandler @Inject constructor() {

    /**
     * @param exception exception thrown during business logic.
     *
     * @return error type handled by the UI layer.
     * [UIError] is a sealed class, so the view knows all the possible errors.
     */
    fun handleError(exception: Exception) : UIError {
        return when (exception) {
            is AlreadyRegisteredUserException -> UIError.AlreadyRegistered
            is UserNotFoundException -> UIError.UserNotFound
            is EmptyUserException -> UIError.EmptyField
            is UnknownHostException -> UIError.NetworkError
            else -> UIError.GenericError
        }
    }

    // Represents error types. Each one is handled in a different way on the UI.
    sealed class UIError {
        object AlreadyRegistered: UIError()
        object UserNotFound: UIError()
        object EmptyField: UIError()
        object NetworkError : UIError()
        object GenericError : UIError()
    }
}