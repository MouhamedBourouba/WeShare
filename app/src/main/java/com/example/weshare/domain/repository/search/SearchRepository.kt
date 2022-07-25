package com.example.weshare.domain.repository.search

import com.example.weshare.data.Result
import com.example.weshare.domain.model.User

interface SearchRepository {
    suspend fun getUsers(): Result<List<User>>
}