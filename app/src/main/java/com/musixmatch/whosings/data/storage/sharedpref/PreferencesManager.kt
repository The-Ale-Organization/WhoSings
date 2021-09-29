package com.musixmatch.whosings.data.storage.sharedpref

interface PreferencesManager {

    fun getEnrolledUser(): String?

    fun saveEnrolledUser(username: String)

    fun clear()

}