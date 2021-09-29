package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.data.exception.EmptyUserException
import com.musixmatch.whosings.data.exception.UserNotFoundException
import com.musixmatch.whosings.data.repository.UserRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    fun enrollUser(userName: String?) {
        if (!userName.isNullOrBlank()) {
            if (!userRepository.isUserRegistered(userName)) {
                // User is not registered yet.
                throw UserNotFoundException()
            } else {
                // User is already registered.
                // Mark the user as the currently enrolled user.
                userRepository.enrollUser(userName)
            }
        } else {
            throw EmptyUserException()
        }
    }

}

