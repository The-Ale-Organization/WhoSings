package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.storage.disk.PreferencesManager
import com.musixmatch.whosings.data.storage.ram.VolatileMemoryManager
import javax.inject.Inject

class ClearSessionUseCase @Inject constructor(
    private val volatileMemoryManager: VolatileMemoryManager,
    private val preferencesManager: PreferencesManager
) {

    fun clearSessionData() {
        volatileMemoryManager.clear()
        preferencesManager.clear()
    }

}

