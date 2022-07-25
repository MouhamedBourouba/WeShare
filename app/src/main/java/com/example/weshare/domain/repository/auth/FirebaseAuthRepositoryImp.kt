package com.example.weshare.domain.repository.auth

import android.util.Log
import com.example.weshare.data.Result
import com.example.weshare.data.localdb.RoomDataBase
import com.example.weshare.domain.model.AuthUser
import com.example.weshare.domain.model.User
import com.example.weshare.domain.utils.getLastUserElement
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await


class FirebaseAuthRepositoryImp(
    private val roomDataBase: RoomDataBase,
) : AuthRepository {


    private val authService = Firebase.auth
    private val dbService = Firebase.firestore.collection("User")
    private val roomUserDto = roomDataBase.getUserDao()

    private suspend fun uploadUserToFireStore(user: User,): Result<User> {
        return try {
            var isSuccess = false
            var errorMessage = ""

            dbService.document(user.uid)
                .set(user)
                .addOnSuccessListener {
                    isSuccess = true
                }
                .addOnFailureListener {
                    errorMessage = it.message.toString()
                }
                .await()

            if (isSuccess) Result.Success(user) else Result.Error(errorMessage)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown Error")
        }
    }

    private suspend fun insertUserToRoom(user: User) {
        return roomDataBase.getUserDao().createUser(user)
    }


    private suspend fun getUserData(uid: String): Result<User> {
        return try {
            var user: User
            var result: Result<User> = Result.Error("")

            dbService.document(uid).get()
                .addOnSuccessListener {
                    if (it.data != null) {
                        user = it.toObject(User::class.java)!!
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
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown Error")
        }

    }

    override suspend fun createUser(authUser: AuthUser): Result<User> {
        return try {
            var isSuccess = false
            var user = User()
            var errorMessage = ""


            authService.createUserWithEmailAndPassword(authUser.email, authUser.password)
                .addOnSuccessListener { authTask ->
                    user = User(
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
                        insertUserToRoom(user)
                        Result.Success(user)
                    }
                    is Result.Error -> {
                        Result.Error(uploadUserTask.message ?: "")
                    }
                }
            } else Result.Error(errorMessage)
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown Error")
        }

    }

    override suspend fun singInUser(authUser: AuthUser): Result<User> {
        return try {
            var isSingInSuccess = false
            var errorMessage = ""
            var uid = ""
            authService.signInWithEmailAndPassword(authUser.email, authUser.password)
                .addOnFailureListener {
                    errorMessage = it.message.toString()
                }
                .addOnSuccessListener {
                    uid = it.user?.uid!!
                    isSingInSuccess = true
                }
                .await()
            if (!isSingInSuccess) {
                Result.Error(errorMessage)
            } else {
                when (val userTaskResult = getUserData(uid)) {
                    is Result.Error -> {
                        Result.Error(userTaskResult.message!!)
                    }
                    is Result.Success -> {
                        insertUserToRoom(userTaskResult.data!!)
                        Result.Success(userTaskResult.data)
                    }
                }
            }
        } catch (e: Exception) {
            Result.Error(e.message ?: "Unknown Error")
        }

    }

    override suspend fun googleAuth(credential: AuthCredential): Result<User> {
        var isSuccess = false
        var authResult: AuthResult? = null

        authService.signInWithCredential(credential)
            .addOnSuccessListener {
                if (it.user != null) {
                    isSuccess = true
                    authResult = it
                }
            }
            .await()


        return if (isSuccess) {
            val userData = dbService.document(authService.uid!!).get().await().toObject(User::class.java)
            if (userData == null) {
                when (
                    val uploadUserToFireBaseResult = uploadUserToFireStore(
                        User(
                            username = authResult!!.user!!.displayName ?: return Result.Error("Error Please Try Again"),
                            email = authResult!!.user!!.email!!,
                            uid = authResult!!.user!!.uid
                        )
                    )
                ) {
                    is Result.Error -> Result.Error(uploadUserToFireBaseResult.message!!)
                    is Result.Success -> {
                        insertUserToRoom(uploadUserToFireBaseResult.data!!)
                        Result.Success(uploadUserToFireBaseResult.data)
                    }
                }
            }
            else {
                insertUserToRoom(userData)
                Result.Success(userData)
            }
        } else Result.Error("Error Please Try Again")
    }

    override suspend fun isAuthenticated(): User? {
        val user = roomUserDto.getUser().getLastUserElement()
        Log.d("TAG", "isAuthenticated: $user")

        return user
    }
}