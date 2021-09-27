package com.musixmatch.whosings.data.storage.sharedpref

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PreferencesManager @Inject constructor(@ApplicationContext private val context: Context) {

    private val prefs = context.getSharedPreferences(WHO_SINGS_PREFS, Context.MODE_PRIVATE)

    private val USERNAME = "USERNAME"

    fun getEnrolledUser(): String? {
        return prefs.getString(USERNAME, null)
    }

    fun saveEnrolledUser(username: String) {
        prefs.edit().putString(USERNAME, username).apply()
    }

    fun clear() {
        prefs.edit().putString(USERNAME, null).apply()
    }

    companion object {
        const val WHO_SINGS_PREFS = "WHO_SINGS_PREFS"
    }
}