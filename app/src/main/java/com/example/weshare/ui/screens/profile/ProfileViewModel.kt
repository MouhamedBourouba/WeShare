package com.example.weshare.ui.screens.profile

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weshare.data.Result
import com.example.weshare.domain.model.User
import com.example.weshare.domain.repository.Profile.ProfileRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    val profileRepository: ProfileRepository
) : ViewModel() {

    var loading by mutableStateOf(true)
    var user by mutableStateOf(User())
    var getCurrentAccount by mutableStateOf<Boolean?>(null)

    init {
        viewModelScope.launch {
            loading = true
            user = profileRepository.getYourAccount()
            loading = false
        }
    }

    fun getCurrentAccount() = viewModelScope.launch {
        loading = true
        user = profileRepository.getYourAccount()
        loading = false
    }

    fun getUserAccountByUid(uid: String) = viewModelScope.launch {
        loading = true
        when (val getUserAccountResult = profileRepository.getUserByUid(uid)) {
            is Result.Error -> {

            }
            is Result.Success -> {
                user = getUserAccountResult.data!!
            }
        }
        loading = false
    }

}