package com.musixmatch.whosings.common.data.storage.disk

interface PreferencesManager {

    fun getEnrolledUser(): String?

    fun saveEnrolledUser(username: String)

    fun clear()

}