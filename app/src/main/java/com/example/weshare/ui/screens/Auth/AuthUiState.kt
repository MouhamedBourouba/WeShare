package com.example.weshare.ui.screens.Auth


data class AuthUiState(
    var userName: String = "",
    var email: String = "",
    var password: String = "",
    val isLogin: Boolean = true,
    var isSingInEnable: Boolean = false,
    var isSingUpEnable: Boolean = false,
    var isGoogleAuth: Boolean = false,
)