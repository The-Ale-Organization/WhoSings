package com.musixmatch.whosings.home.business.usecase

import com.musixmatch.whosings.common.data.storage.disk.PreferencesManager
import com.musixmatch.whosings.common.data.storage.ram.VolatileMemoryManager
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

