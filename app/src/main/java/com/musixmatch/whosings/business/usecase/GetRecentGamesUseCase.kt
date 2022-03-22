package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.exception.UserNotFoundException
import com.musixmatch.whosings.common.data.repository.UserRepository
import com.musixmatch.whosings.common.data.model.entity.ScoreEntity
import javax.inject.Inject

class GetRecentGamesUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    fun getRecentGames(): List<ScoreEntity> {
        val enrolledUserName = userRepository.getEnrolledUserName()
        enrolledUserName?.let { u ->
            val userEntity = userRepository.getUserByName(u)
            userEntity?.let { user ->
                return user.scores?.sortedByDescending { it.time } ?: listOf()
            } ?: throw UserNotFoundException()
        } ?: throw UserNotFoundException()
    }

}

