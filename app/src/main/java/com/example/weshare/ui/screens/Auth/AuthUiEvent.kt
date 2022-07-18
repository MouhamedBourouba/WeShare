package com.example.weshare.ui.screens.Auth

sealed class AuthUiEvent() {
    data class UserNameTextChanged(val text: String) : AuthUiEvent()
    data class EmailTextChanged(val text: String) : AuthUiEvent()
    data class PasswordTextChanged(val text: String) : AuthUiEvent()
    object ChangeScreen: AuthUiEvent()
    object OnSingInBtnClicked : AuthUiEvent()
}
