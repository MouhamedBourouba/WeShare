package com.example.weshare.common

sealed interface UpdateTaskType {
    object UpdateUsername: UpdateTaskType
    object UpdateImage: UpdateTaskType
    object UpdateBio: UpdateTaskType
}
