package com.example.weshare.ui.screens.Auth

import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.AuthCredential

sealed class AuthUiEvent() {
    data class UserNameTextChanged(val text: String) : AuthUiEvent()
    data class EmailTextChanged(val text: String) : AuthUiEvent()
    data class PasswordTextChanged(val text: String) : AuthUiEvent()
    object ChangeScreen: AuthUiEvent()
    object OnSingInBtnClicked : AuthUiEvent()
    data class OnGoogleAuth(val googleSignInAccount: AuthCredential) : AuthUiEvent()
}
