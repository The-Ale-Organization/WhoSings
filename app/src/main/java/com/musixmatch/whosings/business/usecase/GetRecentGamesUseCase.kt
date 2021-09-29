package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.exception.UserNotFoundException
import com.musixmatch.whosings.data.repository.UserRepository
import com.musixmatch.whosings.data.storage.room.ScoreEntity
import javax.inject.Inject

class GetRecentGamesUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    fun getRecentGames(): List<ScoreEntity> {
        val enrolledUserName = userRepository.getEnrolledUserName()
        enrolledUserName?.let { u ->
            val userEntity = userRepository.getUserByName(u)
            userEntity?.let { user ->
                return user.scores?.sortedWith(
                    compareBy({ it.year }, { it.month }, { it.day })
                ) ?: listOf()
            } ?: throw UserNotFoundException()
        } ?: throw UserNotFoundException()
    }

}

