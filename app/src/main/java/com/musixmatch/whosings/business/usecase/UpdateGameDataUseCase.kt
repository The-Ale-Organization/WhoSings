package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.repository.MusicRepository
import com.musixmatch.whosings.data.repository.UserRepository
import java.util.*
import javax.inject.Inject

class UpdateGameDataUseCase @Inject constructor(
    private val userRepository: UserRepository,
    private val musicRepository: MusicRepository
) {

    fun updateData(score: Int) {
        // Add the noew score to db.
        val userName = userRepository.getEnrolledUserName()
        userName?.let {
            val cal = Calendar.getInstance(TimeZone.getTimeZone("Europe/Paris"))
            cal.time = Date()
            val year = cal[Calendar.YEAR]
            val month = cal[Calendar.MONTH] + 1
            val day = cal[Calendar.DAY_OF_MONTH]
            userRepository.updateUser(
                userName = it,
                score = score,
                day = day.toString(),
                month = month.toString(),
                year = year.toString()
            )
        }
        // Clear songs: at each new game a new set of songs is used.
        musicRepository.clearSongs()
    }

}

