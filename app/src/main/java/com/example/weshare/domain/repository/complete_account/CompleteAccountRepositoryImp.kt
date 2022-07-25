package com.example.weshare.domain.repository.complete_account

import android.net.Uri
import com.example.weshare.data.Result
import com.example.weshare.data.localdb.RoomDataBase
import com.example.weshare.domain.model.User
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.UploadTask
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await
import java.util.*

class CompleteAccountRepositoryImp(
    roomDataBase: RoomDataBase,
) : CompleteAccountRepository {

    private val fireStore = Firebase.firestore.collection("User")
    private val auth = Firebase.auth
    private val storage =
        Firebase.storage.getReference("users_accounts_images/${UUID.randomUUID()}")
    private val roomDataBaseDto = roomDataBase.getUserDao()


    override suspend fun updateAccount(field: String, value: Any): Result<Unit> {
        return try {
            var errorMessage = ""
            var success = false

            val userUid = Firebase.auth.uid

            val mDocument = fireStore
                .whereEqualTo("uid", userUid)
                .get()
                .await()

            mDocument.forEach { queryDocumentSnapshot ->
                fireStore.document(queryDocumentSnapshot.id).update(field, value)
                    .addOnSuccessListener {
                        success = true
                    }
                    .addOnFailureListener { exception ->
                        errorMessage = exception.message ?: "Unknown Error"
                    }
                    .await()
            }


            when (success) {
                true -> {
                    Result.Success(Unit)
                }
                false -> Result.Error(errorMessage)
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown Error")
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


    override suspend fun uploadImage(imageUri: Uri): Result<String> {
        var uploadTask: UploadTask.TaskSnapshot? = null
        var errorMessage = ""

        storage.putFile(imageUri)
            .addOnSuccessListener {
                uploadTask = it
            }
            .addOnFailureListener {
                errorMessage = it.message ?: "Unknown Error"
            }
            .await()

        return when (uploadTask != null) {
            true -> {
                var downloadUrl: String = ""

                uploadTask?.storage?.downloadUrl?.addOnSuccessListener {
                    downloadUrl = it.toString()
                }?.await()

                Result.Success(downloadUrl)
            }
            false -> Result.Error(errorMessage)
        }

    }
}