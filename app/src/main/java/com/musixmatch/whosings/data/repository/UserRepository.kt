package com.musixmatch.whosings.data.repository

import com.musixmatch.whosings.data.storage.room.UserEntity

interface UserRepository {

    fun getRegisteredUsers(): List<UserEntity>

    fun isUserRegistered(username: String): Boolean

    fun registerUser(userName: String)

    fun enrollUser(userName: String)

}