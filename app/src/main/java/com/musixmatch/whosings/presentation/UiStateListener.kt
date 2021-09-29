package com.musixmatch.whosings.presentation

import com.musixmatch.whosings.business.error.ErrorHandler

interface UiStateListener {

    fun showProgress()
    fun hideProgress()
    fun showError(error: ErrorHandler.UIError)

}