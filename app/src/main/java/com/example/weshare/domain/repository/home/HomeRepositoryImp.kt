package com.example.weshare.domain.repository.home

import com.example.weshare.data.localdb.RoomDataBase
import com.example.weshare.domain.model.User
import com.example.weshare.domain.utils.getLastUserElement

class HomeRepositoryImp(
    roomDataBase: RoomDataBase
): HomeRepository {

    private val userDto = roomDataBase.getUserDao()

    override suspend fun getUserFromRoom(): User {
        return userDto.getUser().getLastUserElement()!!
    }
}