package com.example.weshare.domain.utils

import com.example.weshare.common.Gender
import com.example.weshare.domain.model.User

fun List<User>.getLastUserElement(): User? {
    var lastUserRecord = User(id = Int.MIN_VALUE)

    if (this.isEmpty()) {
        return null
    }

    this.forEach {
        if (it.id > lastUserRecord.id) {
            lastUserRecord = it
        }
    }

    return lastUserRecord
}

fun User.isAccountCompleted(): Boolean {
    return !(this.age == null || this.gender == null || this.imageUrl == null)
}


fun Boolean?.toGander(): Gender? {
    return when (this) {
        true -> Gender.Male
        false -> Gender.Female
        null -> null
    }
}