package com.musixmatch.whosings.business.error

import java.net.UnknownHostException
import javax.inject.Inject

/**
 * A simple error handler, which can be expanded to handle more specific errors.
 *
 */
class ErrorHandler @Inject constructor() {

    /**
     * @param exception exception thrown during business logic.
     * @return error type handled by the UI layer.
     */
    fun handleError(exception: Exception) : UIError {
        return when (exception) {
            is UnknownHostException -> UIError.NetworkError
            else -> UIError.GenericError
        }
    }

    // Represents error types. Each one is handled in a different way on the UI.
    sealed class UIError {
        object NetworkError : UIError()
        object GenericError : UIError()
    }
}