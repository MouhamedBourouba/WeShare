package com.example.weshare.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class FBUser(
    @PrimaryKey(autoGenerate = false)
    val uid: String = "",
    @ColumnInfo(name = "username")
    val username: String = "",
    @ColumnInfo(name = "email")
    val email: String = "",
    @ColumnInfo(name = "imageUrl")
    val imageUrl: String? = "",
    @ColumnInfo(name = "isOnline")
    val isOnline: Boolean = false,
    @ColumnInfo(name = "followers")
    val followers: ArrayList<String> = arrayListOf(),
    @ColumnInfo(name = "following")
    val following: ArrayList<String> = arrayListOf(),
) : Parcelable