package com.musixmatch.whosings.common.data.repository

import com.musixmatch.whosings.common.data.model.entity.ScoreEntity
import com.musixmatch.whosings.common.data.storage.disk.UserDao
import com.musixmatch.whosings.common.data.model.entity.UserEntity
import com.musixmatch.whosings.common.data.storage.disk.PreferencesManager
import kotlin.math.max

class UserRepositoryImpl constructor(
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
                scores = mutableListOf(),
                bestScore = null
            )
        )
    }

    override fun enrollUser(userName: String) {
        preferencesManager.saveEnrolledUser(userName)
    }

    override fun getEnrolledUserName(): String? {
        return preferencesManager.getEnrolledUser()
    }

    override fun updateUser(userName: String, score: Int, time: Long) {
        getUserByName(userName)?.let { user ->
            // Calculate new best score.
            val bestScore = max(score, user.scores?.map { it.score }?.maxOrNull() ?: 0)
            // Add the new score to the list.
            val updatedScores: MutableList<ScoreEntity> = user.scores?.toMutableList() ?: mutableListOf()
            updatedScores.add(
                ScoreEntity(
                score = score,
                time = time
                )
            )
            val updatedUser = user.copy(
                username = user.username,
                avatarUrl = user.avatarUrl,
                scores = updatedScores,
                bestScore = bestScore
            )
            // Update user in db.
            userDao.insert(
                user = updatedUser
            )
        }
    }

}