package com.example.weshare.domain.repository.Profile

import com.example.weshare.data.Result
import com.example.weshare.data.localdb.RoomDataBase
import com.example.weshare.domain.model.User
import com.example.weshare.domain.utils.getLastUserElement
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class ProfileRepositoryImp(
    roomDataBase: RoomDataBase
) : ProfileRepository {

    private val fireStore = Firebase.firestore.collection("User")
    private val roomUserDto = roomDataBase.getUserDao()

    override suspend fun getYourAccount(): User {
        return roomUserDto.getUser().getLastUserElement()!!
    }

    override suspend fun getUserByUid(uid: String): Result<User> {
        return try {
            val user = fireStore.document(uid).get().await()
            if (user.data == null) {
                Result.Error("No User Data Please Check You Internet")
            } else {
                Result.Success(user.toObject(User::class.java)!!)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown Error Please Try Again")
        }
    }
}