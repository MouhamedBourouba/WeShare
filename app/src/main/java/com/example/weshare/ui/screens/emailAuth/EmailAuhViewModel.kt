package com.example.weshare.ui.screens.emailAuth

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EmailAuhViewModel @Inject constructor() : ViewModel() {
    var uiState = mutableStateOf(EmailAuthUiState())

    fun onEvent(event: EmailAuthUiEvent) {
        when (event) {
            is EmailAuthUiEvent.EmailTextChanged -> {
                uiState.value = uiState.value.copy(email = event.text)
                enableButton()
            }
            is EmailAuthUiEvent.UserNameTextChanged -> {
                uiState.value = uiState.value.copy(userName = event.text)
                enableButton()
            }
            is EmailAuthUiEvent.PasswordTextChanged -> {
                uiState.value = uiState.value.copy(password = event.text)
                enableButton()
            }
            is EmailAuthUiEvent.ChangeScreen -> {
                uiState.value = uiState.value.copy(isLogin = !uiState.value.isLogin)
            }
            is EmailAuthUiEvent.OnSingInBtnClicked -> {
                if (uiState.value.isLogin) singIn() else singUp()
            }
        }
    }

    private fun singUp() {



    }

    private fun singIn() {



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