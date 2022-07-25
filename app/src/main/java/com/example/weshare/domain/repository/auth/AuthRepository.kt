package com.example.weshare.domain.repository.auth

import com.example.weshare.data.Result
import com.example.weshare.domain.model.AuthUser
import com.example.weshare.domain.model.User
import com.google.firebase.auth.AuthCredential

interface AuthRepository {
    suspend fun createUser(authUser: AuthUser): Result<User>
    suspend fun singInUser(authUser: AuthUser): Result<User>
    suspend fun googleAuth(credential: AuthCredential): Result<User>
    suspend fun isAuthenticated(): User?
}