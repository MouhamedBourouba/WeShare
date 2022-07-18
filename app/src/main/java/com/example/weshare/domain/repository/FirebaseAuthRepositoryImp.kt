package com.example.weshare.domain.repository

import com.example.weshare.data.Result
import com.example.weshare.domain.model.AuthUser
import com.example.weshare.domain.model.FBUser
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class FirebaseAuthRepositoryImp : AuthRepository {
    private val authService = Firebase.auth
    private val dbService = Firebase.firestore.collection("User")

    private suspend fun uploadUserToFireStore(fbUser: FBUser): Result<Unit> {
        return try {
            var isSuccess = false
            var errorMessage = ""
            dbService.document(fbUser.uid)
                .set(fbUser)
                .addOnSuccessListener {
                    isSuccess = true
                }
                .addOnFailureListener {
                    errorMessage = it.message.toString()
                }
                .await()
            if (isSuccess) Result.Success(Unit) else Result.Error(errorMessage)
        } catch (e: FirebaseNetworkException) {
            Result.Error("No Internet Connection!!")
        } catch (e: Exception) {
            Result.Error("Unknown Error")
        }
    }

    private suspend fun getUserData(uid: String): Result<FBUser> {
        return try {
            var user: FBUser
            var result: Result<FBUser> = Result.Error("")

            dbService.document(uid).get()
                .addOnSuccessListener {
                    if (it != null) {
                        user = it.toObject(FBUser::class.java)!!
                        result = Result.Success(user)
                    } else {
                        result = Result.Error("NO USER DATA")
                    }
                }
                .addOnFailureListener {
                    result = Result.Error(it.message.toString())
                }
                .await()
            result
        } catch (e: FirebaseNetworkException) {
            Result.Error("No Internet Connection!!")
        } catch (e: Exception) {
            Result.Error("Unknown Error")
        }

    }

    override suspend fun createUser(authUser: AuthUser): Result<Unit> {
        return try {
            var isSuccess = false
            var user = FBUser()
            var errorMessage = ""


            authService.createUserWithEmailAndPassword(authUser.email, authUser.password)
                .addOnSuccessListener { authTask ->
                    user = FBUser(
                        uid = authTask.user?.uid!!,
                        username = authUser.username,
                        email = authTask.user?.email!!,
                        imageUrl = null,
                        isOnline = false,
                        followers = arrayListOf(),
                        following = arrayListOf(),
                    )
                    isSuccess = true
                }
                .addOnFailureListener {
                    isSuccess = false
                    errorMessage = it.message.toString()
                }
                .await()

            if (isSuccess) {
                when (val uploadUserTask = uploadUserToFireStore(user)) {
                    is Result.Success -> {
                        Result.Success(Unit)
                    }
                    is Result.Error -> {
                        Result.Error(uploadUserTask.message ?: "")
                    }
                }
            } else Result.Error(errorMessage)
        } catch (e: FirebaseNetworkException) {
            Result.Error("No Internet Connection!!")
        } catch (e: Exception) {
            Result.Error("Unknown Error")
        }

    }

    override suspend fun singInUser(authUser: AuthUser): Result<FBUser> {
        return try {
            var isSingInSuccess = false
            var errorMessage = ""
            var uid = ""
            authService.signInWithEmailAndPassword(authUser.email, authUser.password)
                .addOnSuccessListener {
                    uid = it.user?.uid!!
                    isSingInSuccess = true
                }
                .addOnFailureListener {
                    errorMessage = it.message.toString()
                }
                .await()
            if (!isSingInSuccess) {
                Result.Error(errorMessage)
            } else {
                getUserData(uid)
            }
        } catch (e: FirebaseNetworkException) {
            Result.Error("No Internet Connection!!")
        } catch (e: Exception) {
            Result.Error("Unknown Error")
        }

    }


    override suspend fun isAuthenticated(): Result<Unit> {
        return try {
            if (authService.currentUser != null) {
                Result.Success(Unit)
            } else {
                Result.Error("")
            }
        } catch (e: FirebaseNetworkException) {
            Result.Error("No Internet Connection!!")
        } catch (e: Exception) {
            Result.Error("Unknown Error")
        }
    }
}