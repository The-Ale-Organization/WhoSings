package com.musixmatch.whosings.business.usecase

import com.musixmatch.whosings.business.error.AlreadyRegisteredUserException
import com.musixmatch.whosings.business.error.EmptyUserException
import com.musixmatch.whosings.data.repository.UserRepository
import javax.inject.Inject

class RegistrationUseCase @Inject constructor(
    private val userRepository: UserRepository
) {

    fun registerUser(userName: String?) {
        if (!userName.isNullOrBlank()) {
            if (!userRepository.isUserRegistered(userName)) {
                // User is not registered yet. Add it to the db.
                userRepository.registerUser(userName)
                // Mark the user as the currently enrolled user.
                userRepository.enrollUser(userName)
            } else {
                // User is already registered.
                throw AlreadyRegisteredUserException()
            }
        } else {
            throw EmptyUserException()
        }
    }

}

