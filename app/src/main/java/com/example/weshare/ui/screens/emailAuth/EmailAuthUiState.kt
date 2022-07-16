package com.example.weshare.ui.screens.emailAuth


data class EmailAuthUiState(
    var userName: String = "",
    var email: String = "",
    var password: String = "",
    val isLogin: Boolean = true,
    var isSingInEnable: Boolean = false,
    var isSingUpEnable: Boolean = false,
)