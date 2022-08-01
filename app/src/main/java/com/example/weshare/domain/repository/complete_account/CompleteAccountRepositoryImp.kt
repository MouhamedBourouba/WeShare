package com.example.weshare.domain.repository.complete_account

import android.net.Uri
import com.example.weshare.data.Result
import com.example.weshare.data.localdb.RoomDataBase
import com.example.weshare.domain.model.User
import com.example.weshare.domain.utils.getLastUserElement
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*

class CompleteAccountRepositoryImp(
    roomDataBase: RoomDataBase,
) : CompleteAccountRepository {

    private val fireStore = Firebase.firestore.collection("User")
    private val auth = Firebase.auth
    private val storage =
        Firebase.storage.getReference("users_accounts_images/${UUID.randomUUID()}")
    private val roomDataBaseDto = roomDataBase.getUserDao()


    override suspend fun updateAccount(field: String, value: Any) = channelFlow <Result<Unit>> {
        withContext(Dispatchers.IO) {
            val task = fireStore.document(auth.uid!!).update(field, value)
            task.await()
            if (task.isSuccessful) {
                send(Result.Success(Unit))
            } else send(Result.Error(task.exception?.message ?: "Unknown Error"))
        }
    }

    override suspend fun uploadImage(imageUri: Uri) = channelFlow {
        withContext(Dispatchers.IO) {
            val uploadImageTask = storage.putFile(imageUri)

            uploadImageTask.await()

            if (uploadImageTask.isSuccessful) {
                val downloadUrl = uploadImageTask.result.storage.downloadUrl

                downloadUrl.await()

                if (downloadUrl.isSuccessful) send(Result.Success(downloadUrl.result.toString()))
                else send(Result.Error(downloadUrl.exception?.message ?: "Unknown Error"))
            }
        }
    }

    override suspend fun updateRoomDbUser(user: User) {
        roomDataBaseDto.createUser(user)
    }

    override suspend fun getUserFromFireStore(): User {
        return fireStore.document(auth.uid!!)
            .get()
            .await()
            .toObject(User::class.java)!!
    }


    override suspend fun getUserFromRoom(): User {
        return roomDataBaseDto.getUser().getLastUserElement()!!
    }
}
