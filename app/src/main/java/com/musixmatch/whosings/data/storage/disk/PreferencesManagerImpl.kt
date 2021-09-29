package com.musixmatch.whosings.data.storage.disk

import android.content.Context


class PreferencesManagerImpl (context: Context) : PreferencesManager {

    private val prefs = context.getSharedPreferences(WHO_SINGS_PREFS, Context.MODE_PRIVATE)

    private val USERNAME = "USERNAME"

    override fun getEnrolledUser(): String? {
        return prefs.getString(USERNAME, null)
    }

    override fun saveEnrolledUser(username: String) {
        prefs.edit().putString(USERNAME, username).apply()
    }

    override fun clear() {
        prefs.edit().putString(USERNAME, null).apply()
    }

    companion object {
        const val WHO_SINGS_PREFS = "WHO_SINGS_PREFS"
    }
}