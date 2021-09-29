package com.musixmatch.whosings.data.storage.disk

interface PreferencesManager {

    fun getEnrolledUser(): String?

    fun saveEnrolledUser(username: String)

    fun clear()

}