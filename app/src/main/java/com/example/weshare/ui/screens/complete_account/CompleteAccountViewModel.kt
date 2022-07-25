package com.example.weshare.ui.screens.complete_account

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weshare.data.Result
import com.example.weshare.data.localdb.RoomDataBase
import com.example.weshare.domain.model.User
import com.example.weshare.domain.repository.complete_account.CompleteAccountRepository
import com.example.weshare.domain.utils.isAccountCompleted
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CompleteAccountViewModel @Inject constructor(
    private val completeAccountRepository: CompleteAccountRepository,
) : ViewModel() {

    var loading by mutableStateOf(false)
    var user by mutableStateOf(User())
    var imageUri by mutableStateOf<Uri?>(null)
    var createUi =  mutableStateOf(false)

    private val _taskChannel = Channel<Result<Boolean>>()
    val taskChannel get() = _taskChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            user = completeAccountRepository.getUserFromFireStore()
            createUi.value = true
        }
        viewModelScope.launch {
            taskChannel.collect {
                // check if user Completed all steps
                if (it.data == true) {
                    completeAccountRepository.updateRoomDbUser(user.copy(id = 0))
                }
            }
        }
    }

    fun uploadImage() {
        viewModelScope.launch {
            loading = true
            when (val uploadImageResult = completeAccountRepository.uploadImage(imageUri!!)) {
                is Result.Error -> {
                    _taskChannel.send(Result.Error(uploadImageResult.message!!))
                }
                is Result.Success -> {
                    user.imageUrl = uploadImageResult.data

                    when (val updateAccountResult = completeAccountRepository.updateAccount(
                        "imageUrl",
                        uploadImageResult.data!!
                    )) {
                        is Result.Error -> _taskChannel.send(Result.Error(updateAccountResult.message!!))
                        is Result.Success -> _taskChannel.send(Result.Success(user.isAccountCompleted()))
                    }
                }
            }
            loading = false
        }
    }

    fun updateAccount(field: String, value: Any) = viewModelScope.launch {
        if (loading) return@launch

        loading = true
        when (val updateResult = completeAccountRepository.updateAccount(field, value)) {
            is Result.Error -> _taskChannel.send(
                Result.Error(
                    updateResult.message ?: "Unknown Error"
                )
            )
            is Result.Success -> {
                _taskChannel.send(
                    when (field) {
                        "age" -> {
                            user.age = value.toString().toInt()
                            Result.Success(user.isAccountCompleted())
                        }
                        "gender" -> {
                            user.gender = value.toString().toBoolean()
                            Result.Success(user.isAccountCompleted())
                        }
                        else -> Result.Success(user.isAccountCompleted())
                    }
                )
            }
        }
        loading = false
    }
}