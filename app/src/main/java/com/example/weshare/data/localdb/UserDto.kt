package com.example.weshare.data.localdb

import androidx.room.*
import com.example.weshare.domain.model.User

@Dao
interface UserDto {
    @Query("SELECT * FROM currentUser")
    suspend fun getUser(): List<User>
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun createUser(user: User)
    @Delete
    suspend fun deleteUser(user: User)
}
