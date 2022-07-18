package com.example.weshare.domain.repository

import com.example.weshare.domain.model.FBUser
import com.example.weshare.data.Result
import com.example.weshare.domain.model.AuthUser

interface AuthRepository {
    suspend fun createUser(authUser: AuthUser): Result<Unit>
    suspend fun singInUser(authUser: AuthUser): Result<FBUser>
    suspend fun isAuthenticated(): Result<Unit>
}