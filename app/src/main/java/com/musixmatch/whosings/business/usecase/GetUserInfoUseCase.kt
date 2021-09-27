package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.business.error.UserNotFoundException
import com.musixmatch.whosings.data.model.UserInfo
import com.musixmatch.whosings.data.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    fun getUser(): UserInfo {
        val enrolledUserName = userRepository.getEnrolledUserName()
        enrolledUserName?.let {
            val userEntity = userRepository.getUserByName(it)
            userEntity?.let { user ->
                // Get overall position.
                val users = userRepository.getRegisteredUsers()
                val position = userRepository.getRegisteredUsers()
                    ?.sortedBy { u -> u.bestScore }?.indexOf(user)

                return UserInfo(
                    username = userEntity.username,
                    bestScore = userEntity.bestScore,
                    rankingPosition = position?.plus(1),
                    totalUsers = users?.count(),
                    avatarUrl = ""
                )
            } ?: throw UserNotFoundException()
        } ?: throw UserNotFoundException()
    }

}

