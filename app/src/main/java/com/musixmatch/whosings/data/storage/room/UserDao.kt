package com.musixmatch.whosings.data.storage.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<UserEntity>

    @Query("SELECT * FROM user WHERE userName IN (:userName)")
    fun getByName(userName: String): UserEntity?

    @Insert
    fun insert(user: UserEntity)


}
