package com.musixmatch.whosings.data.storage.disk

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.musixmatch.whosings.data.model.entity.UserEntity

@Dao
interface UserDao {

    @Query("SELECT * FROM user")
    fun getAll(): List<UserEntity>?

    @Query("SELECT * FROM user WHERE userName IN (:userName)")
    fun getByName(userName: String): UserEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(user: UserEntity)

}
