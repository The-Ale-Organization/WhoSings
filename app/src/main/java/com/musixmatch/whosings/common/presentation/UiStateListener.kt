package com.musixmatch.whosings.common.presentation

import com.musixmatch.whosings.common.business.error.ErrorHandler

interface UiStateListener {

    fun showProgress()
    fun hideProgress()
    fun showError(error: ErrorHandler.UIError)

}