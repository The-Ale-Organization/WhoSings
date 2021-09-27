package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.repository.MusicRepository
import com.musixmatch.whosings.data.repository.UserRepository
import javax.inject.Inject

class ClearSessionUseCase @Inject constructor(
    private val musicRepository: MusicRepository,
    private val userRepository: UserRepository
) {

    fun clearSessionData() {
        musicRepository.clearSessionData()
        userRepository.clearSessionData()
    }

}

