package com.example.weshare.domain.repository.home

import com.example.weshare.domain.model.User

interface HomeRepository {
    suspend fun getUserFromRoom(): User
}