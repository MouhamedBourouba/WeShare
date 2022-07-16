package com.example.weshare.ui.screens.emailAuth

sealed class EmailAuthUiEvent() {
    data class UserNameTextChanged(val text: String) : EmailAuthUiEvent()
    data class EmailTextChanged(val text: String) : EmailAuthUiEvent()
    data class PasswordTextChanged(val text: String) : EmailAuthUiEvent()
    object ChangeScreen: EmailAuthUiEvent()
    object OnSingInBtnClicked : EmailAuthUiEvent()
}
