package com.example.weshare.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "currentUser")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val uid: String = "",
    val username: String = "",
    val email: String = "",
    var imageUrl: String? = null,
    val isOnline: Boolean = false,
    var age: Int? = null,
    var posts: List<String> = arrayListOf(),
    var bio: String = "No Bio",
    var gender: Boolean? = null,
    val followers: List<String> = arrayListOf(),
    val following: List<String> = arrayListOf(),
) : Parcelable


class ArrayListTypeConverter {
    @TypeConverter
    fun listenForJson(value: List<String>) =
        Gson().toJson(value)!!

    @TypeConverter
    fun jsonToList(value: String): List<String>? =
        Gson().fromJson(value, arrayListOf<String>()::class.java)
}