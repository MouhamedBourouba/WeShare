package com.example.weshare.domain.repository.complete_account

import android.net.Uri
import com.example.weshare.data.Result
import com.example.weshare.domain.model.User
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.flow.Flow

interface CompleteAccountRepository {
    suspend fun updateRoomDbUser(user: User)
    suspend fun getUserFromFireStore(): User
    suspend fun getUserFromRoom(): User
    suspend fun updateAccount(field: String, value: Any): Flow<Result<Unit>>
    suspend fun uploadImage(imageUri: Uri): Flow<Result<String>>
}