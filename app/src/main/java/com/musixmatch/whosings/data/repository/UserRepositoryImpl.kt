package com.musixmatch.whosings.data.repository

import com.musixmatch.whosings.data.storage.room.UserDao
import com.musixmatch.whosings.data.storage.room.UserEntity
import com.musixmatch.whosings.data.storage.sharedpref.PreferencesManager
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val preferencesManager: PreferencesManager,
    private val userDao: UserDao
) : UserRepository {

    override fun getRegisteredUsers(): List<UserEntity>? {
        return userDao.getAll()
    }

    override fun isUserRegistered(username: String): Boolean {
        return userDao.getByName(username) != null
    }

    override fun getUserByName(username: String): UserEntity? {
        return userDao.getByName(username)
    }

    override fun registerUser(userName: String) {
        userDao.insert(
            UserEntity(
                username = userName,
                avatarUrl = null,
                scores = listOf(),
                bestScore = null
            )
        )
    }

    override fun enrollUser(userName: String) {
        preferencesManager.saveEnrolledUser(userName)
    }

    override fun getEnrolledUser(): String? {
        return preferencesManager.getEnrolledUser()
    }
}