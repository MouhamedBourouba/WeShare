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

    }

    fun updateAccount(field: String, value: Any) = viewModelScope.launch {

    }
}