package com.example.weshare.domain.repository.search

import com.example.weshare.data.Result
import com.example.weshare.domain.model.User
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class SearchRepositoryImp() : SearchRepository {
    private val fireStoreUsersCollection = Firebase.firestore.collection("User")
    override suspend fun getUsers(): Result<List<User>> {
        return try {
            val usersDocuments = fireStoreUsersCollection.get().await()
            val usersList = arrayListOf<User>()
            usersDocuments.forEach {
                usersList.add(it.toObject(User::class.java))
            }
            Result.Success(usersList)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown Error")
        }
    }
}