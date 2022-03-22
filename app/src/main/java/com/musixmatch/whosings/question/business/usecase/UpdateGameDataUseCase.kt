package com.musixmatch.whosings.question.business.usecase

import com.musixmatch.whosings.common.data.repository.MusicRepository
import com.musixmatch.whosings.common.data.repository.UserRepository
import java.util.*
import javax.inject.Inject

class UpdateGameDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val musicRepository: MusicRepository
) {

    fun updateData(score: Int) {
        // Add the new score to db.
        val userName = userRepository.getEnrolledUserName()
        userName?.let {
            userRepository.updateUser(
                userName = it,
                score = score,
                time = Date().time
            )
        }
        // Clear songs: at each new game a new set of songs is used.
        musicRepository.clearSongs()
    }

}

