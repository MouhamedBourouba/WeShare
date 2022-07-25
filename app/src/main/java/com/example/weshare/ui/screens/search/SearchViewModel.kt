package com.example.weshare.ui.screens.search

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weshare.data.Result
import com.example.weshare.domain.model.User
import com.example.weshare.domain.repository.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(private val searchRepository: SearchRepository) : ViewModel() {
    var loading by mutableStateOf(false)
    var textFieldValue by mutableStateOf("")
    var users by mutableStateOf<List<User>>(listOf())
    var usersSearchList by mutableStateOf<List<User>>(listOf())
    private val _errorChannel = Channel<String>()
    val errorChannel get() = _errorChannel.consumeAsFlow()


    init {
        viewModelScope.launch {
            loading = true
            val gettingUsersResult = searchRepository.getUsers()
            when (gettingUsersResult) {
                is Result.Error -> _errorChannel.send(gettingUsersResult.message!!)
                is Result.Success -> {
                    // bc i want to let them see My Animation ;=)
                    delay(300L)
                    users = gettingUsersResult.data!!
                    usersSearchList = users
                }
            }
            Log.d("search", "${gettingUsersResult.data}: ")
            loading = false
        }
    }


}