package com.example.weshare.ui.screens.edit_account

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weshare.common.UpdateTaskType
import com.example.weshare.data.Result
import com.example.weshare.domain.model.User
import com.example.weshare.domain.repository.complete_account.CompleteAccountRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import okhttp3.internal.wait
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val completeAccountRepository: CompleteAccountRepository
) : ViewModel() {

    var loading by mutableStateOf(true)
    var user by mutableStateOf(User())
    var imageUri by mutableStateOf<Uri?>(null)
    var profilePhotoUrl by mutableStateOf<String?>(null)
    var userName by mutableStateOf("")
    var bio by mutableStateOf("")
    private var _channel = Channel<Result<Unit>>()
    val channel get() = _channel.consumeAsFlow()
    private val TAG = "VIEW"

    init {
        viewModelScope.launch {
            user = completeAccountRepository.getUserFromRoom()
            profilePhotoUrl = user.imageUrl
            userName = user.username
            bio = user.bio
            loading = false

        }
    }

    fun updateAccount() = viewModelScope.launch(Dispatchers.IO) {
        loading = true

        val completedTasksList = arrayListOf<UpdateTaskType>()
        whatToUpdate().forEach {
            Log.d(TAG, "updateAccount: ${it.toString()}")
        }

        val updateFlow: Flow<Result<UpdateTaskType>> = flow {
            if (user.username != userName) completeAccountRepository.updateAccount("username", userName).collect {
                if (it is Result.Success) emit(Result.Success(UpdateTaskType.UpdateUsername))
                else emit(Result.Error(it.message!!))
            }
            if (user.bio != bio) completeAccountRepository.updateAccount("bio", bio).collect {
                if (it is Result.Success) emit(Result.Success(UpdateTaskType.UpdateBio))
                else emit(Result.Error(it.message!!))
            }
            if (imageUri != null) {
                completeAccountRepository.uploadImage(imageUri!!).collect { uploadImage ->
                    if (uploadImage is Result.Success) completeAccountRepository.updateAccount("imageUrl", uploadImage.data!!).collect { updateAccount ->
                        if (updateAccount is Result.Success) emit(Result.Success(UpdateTaskType.UpdateImage))
                        else emit(Result.Error(updateAccount.message!!))
                    }
                    else emit(Result.Error(uploadImage.message!!))
                }
            }
        }


        updateFlow.collect {
            Log.d(TAG, "updateAccount: i RECEIVE AN EMIT LETS GOO ${it.data} ${it.message}")
            if (it is Result.Success) {
                completedTasksList.add(it.data!!)
                if (completedTasksList.size == completedTasksList.size) {
                    _channel.send(Result.Success(Unit))
                    Log.d(TAG, "updateAccount: GG")
                    loading = false
                } else {
                    Log.d(TAG, "updateAccount: WTF BROO")
                }
            } else {
                loading = false
                _channel.send(Result.Error(it.message!!))
            }
        }

    }



    fun updateRoomUser() {

    }

    private fun whatToUpdate(): List<UpdateTaskType> {
        val updateList = arrayListOf<UpdateTaskType>()
        if (userName != user.username) updateList.add(UpdateTaskType.UpdateUsername)
        if (bio != user.bio) updateList.add(UpdateTaskType.UpdateBio)
        if (imageUri != null) updateList.add(UpdateTaskType.UpdateImage)
        return updateList
    }
}