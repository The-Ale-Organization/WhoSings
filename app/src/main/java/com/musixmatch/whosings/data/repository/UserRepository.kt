package com.musixmatch.whosings.data.repository

import com.musixmatch.whosings.data.model.entity.UserEntity

interface UserRepository {

    fun getRegisteredUsers(): List<UserEntity>?

    fun isUserRegistered(username: String): Boolean

    fun getUserByName(username: String): UserEntity?

    fun registerUser(userName: String)

    fun enrollUser(userName: String)

    fun getEnrolledUserName(): String?

    fun updateUser(userName: String, score: Int, time: Long)

}