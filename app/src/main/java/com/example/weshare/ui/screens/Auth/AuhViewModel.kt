package com.example.weshare.ui.screens.Auth

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weshare.data.Result
import com.example.weshare.domain.model.AuthUser
import com.example.weshare.domain.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuhViewModel @Inject constructor(private val authRepository: AuthRepository) :
    ViewModel() {
    var uiState = mutableStateOf(AuthUiState())
    var loading by mutableStateOf<Boolean?>(null)
    private val _errorChannel = Channel<String>()
    private val _successChannel = Channel<Unit>()
    val successChannel get() = _successChannel.receiveAsFlow()
    val errorChannel get() = _errorChannel.receiveAsFlow()


    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailTextChanged -> {
                uiState.value = uiState.value.copy(email = event.text)
                enableButton()
            }
            is AuthUiEvent.UserNameTextChanged -> {
                uiState.value = uiState.value.copy(userName = event.text)
                enableButton()
            }
            is AuthUiEvent.PasswordTextChanged -> {
                uiState.value = uiState.value.copy(password = event.text)
                enableButton()
            }
            is AuthUiEvent.ChangeScreen -> {
                uiState.value = uiState.value.copy(isLogin = !uiState.value.isLogin)
            }
            is AuthUiEvent.OnSingInBtnClicked -> {
                if (uiState.value.isLogin) singIn(
                    AuthUser(
                        uiState.value.userName,
                        uiState.value.email,
                        uiState.value.password
                    )
                ) else singUp(
                    AuthUser(
                        uiState.value.userName,
                        uiState.value.email,
                        uiState.value.password
                    )
                )
            }
        }
    }

    private fun singUp(authUser: AuthUser) = viewModelScope.launch {
        loading = true
        when (val createUserTask = authRepository.createUser(authUser)) {
            is Result.Error -> {
                _errorChannel.send(createUserTask.message ?: "")
                Log.d("user", "singUp: ${createUserTask.message}")
            }
            is Result.Success -> {
                _successChannel.send(Unit)
            }
        }
    }

    private fun singIn(authUser: AuthUser) = viewModelScope.launch {
        val user = authRepository.singInUser(authUser)
        Log.d("user", "singIn: ${user.data} ${user.message}")
    }

    private fun enableButton() {
        uiState.value = uiState.value.copy(
            isSingInEnable = (
                    uiState.value.email.isNotBlank() && uiState.value.password.isNotBlank()
                    ),
            isSingUpEnable = (
                    uiState.value.email.isNotBlank() && uiState.value.password.isNotBlank() && uiState.value.userName.isNotBlank()
                    ),
        )
    }
}