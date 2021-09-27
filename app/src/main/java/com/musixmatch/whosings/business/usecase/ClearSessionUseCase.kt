package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.storage.sharedpref.PreferencesManager
import com.musixmatch.whosings.data.storage.volatile.VolatileMemoryManager
import javax.inject.Inject

class ClearSessionUseCase @Inject constructor(
    private val preferencesManager: PreferencesManager,
    //private val volatileMemoryManager: VolatileMemoryManager
) {

    fun clearSessionData() {
        preferencesManager.clear()
        //volatileMemoryManager.clear()
    }

}

