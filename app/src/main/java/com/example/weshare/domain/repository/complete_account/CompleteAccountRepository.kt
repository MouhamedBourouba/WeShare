package com.example.weshare.domain.repository.complete_account

import android.net.Uri
import com.example.weshare.data.Result
import com.example.weshare.domain.model.User

interface CompleteAccountRepository {
    suspend fun uploadImage(imageUri: Uri): Result<String>
    suspend fun updateAccount(field: String, value: Any): Result<Unit>
    suspend fun updateRoomDbUser(user: User)
    suspend fun getUserFromFireStore(): User
}