package com.example.weshare.common

import com.example.weshare.domain.model.User

sealed class AuthType {
    object GoogleAuth: AuthType()
    object FacebookAuth: AuthType()
    data class EmailAuth(val user: User?): AuthType()
}