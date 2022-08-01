package com.example.weshare.domain.repository.Profile

import com.example.weshare.domain.model.User
import com.example.weshare.data.Result


interface ProfileRepository {
    suspend fun getYourAccount(): User
    suspend fun getUserByUid(uid: String): Result<User>
}