package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.common.exception.UserNotFoundException
import com.musixmatch.whosings.common.data.model.presentation.UserInfo
import com.musixmatch.whosings.common.data.repository.UserRepository
import javax.inject.Inject

class GetUserInfoUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    fun getUserInfo(): UserInfo {
        val enrolledUserName = userRepository.getEnrolledUserName()
        enrolledUserName?.let {
            val userEntity = userRepository.getUserByName(it)
            userEntity?.let { user ->
                // Get overall position.
                val users = userRepository.getRegisteredUsers()
                val activeUsers = users?.filter { u -> u.bestScore != null }
                val position = activeUsers?.sortedByDescending { u -> u.bestScore }?.indexOf(user) ?: -1

                return UserInfo(
                    username = userEntity.username,
                    bestScore = userEntity.bestScore,
                    rankingPosition = if (position < 0) null else position.plus(1),
                    usersWithBestScore = activeUsers?.count(),
                    avatarUrl = ""
                )
            } ?: throw UserNotFoundException()
        } ?: throw UserNotFoundException()
    }

}

