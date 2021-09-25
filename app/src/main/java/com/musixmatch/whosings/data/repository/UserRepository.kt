package com.musixmatch.whosings.data.repository

import com.musixmatch.whosings.data.storage.room.UserEntity

interface UserRepository {

    fun getRegisteredUsers(): List<UserEntity>?

    fun isUserRegistered(username: String): Boolean

    fun getUserByName(username: String): UserEntity?

    fun registerUser(userName: String)

    fun enrollUser(userName: String)

    fun getEnrolledUser(): String?

}