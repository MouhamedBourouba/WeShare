package com.example.weshare.ui.screens.Auth

import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.weshare.data.Result
import com.example.weshare.data.localdb.RoomDataBase
import com.example.weshare.domain.model.AuthUser
import com.example.weshare.domain.model.User
import com.example.weshare.domain.repository.auth.AuthRepository
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuhViewModel @Inject constructor(
    @ApplicationContext private val context: Context,
    private val authRepository: AuthRepository,
    private val roomDataBase: RoomDataBase
) : ViewModel() {

    val createUi = mutableStateOf(false)
    val uiState = mutableStateOf(AuthUiState())
    var loading by mutableStateOf(false)
    private val _errorChannel = Channel<String>()
    private val _authChannel = Channel<User>()
    val authChannel get() = _authChannel.receiveAsFlow()
    val errorChannel get() = _errorChannel.receiveAsFlow()

    init {
        viewModelScope.launch {
            val roomUser = authRepository.isAuthenticated()
            if (roomUser != null) {
                _authChannel.send(roomUser)
                Log.d("TAG", "user EXIST: ")
            } else {
                Log.d("TAG", "user DOSE NOT EXIST: ")
                createUi.value = true
            }
        }
    }

    fun onEvent(event: AuthUiEvent) {
        when (event) {
            is AuthUiEvent.EmailTextChanged -> {
                uiState.value = uiState.value.copy(email = event.text)
                enableButton()
            }
            is AuthUiEvent.UserNameTextChanged -> {
                if (event.text.toCharArray().size < 30) uiState.value =
                    uiState.value.copy(userName = event.text)
                enableButton()
            }
            is AuthUiEvent.PasswordTextChanged -> {
                if (event.text.toCharArray().size < 30) {
                    uiState.value = uiState.value.copy(password = event.text)
                }
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
            is AuthUiEvent.OnGoogleAuth -> {
                googleAuth(event.googleSignInAccount)
            }
        }
    }

    private fun googleAuth(googleSignInAccount: AuthCredential) {
        viewModelScope.launch {
            loading = true
            Log.d("google auth", "googleAuth: start Google Auth")
            when (val googleAuth = authRepository.googleAuth(googleSignInAccount)) {
                is Result.Error -> _errorChannel.send(googleAuth.message!!)
                is Result.Success -> _authChannel.send(googleAuth.data!!)
            }
            Log.d("google auth", "googleAuth: end Google Auth")
            loading = false
        }
    }

    private fun singUp(authUser: AuthUser) = viewModelScope.launch {
        loading = true
        when (val singUpResult = authRepository.createUser(authUser)) {
            is Result.Error -> _errorChannel.send(singUpResult.message ?: "Unknown Error")
            is Result.Success -> _authChannel.send(singUpResult.data!!)
        }
        loading = false
    }

    private fun singIn(authUser: AuthUser) = viewModelScope.launch {
        loading = true
        when (val singInResult = authRepository.singInUser(authUser)) {
            is Result.Error -> _errorChannel.send(singInResult.message ?: "Unknown Error")
            is Result.Success -> _authChannel.send(singInResult.data!!)
        }
        loading = false
    }

    private fun enableButton() {
        uiState.value = uiState.value.copy(
            isSingInEnable = (uiState.value.email.isNotBlank() && uiState.value.password.isNotBlank()),
            isSingUpEnable = (uiState.value.email.isNotBlank() && uiState.value.password.isNotBlank() && uiState.value.userName.isNotBlank()),
        )
    }

    fun getGoogleSingInIntent(): Intent {
        val googleSingInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .requestProfile()
            .requestIdToken("621197040150-pq88n6chlcilc15899vrstfkbnljedo1.apps.googleusercontent.com")
            .requestId()
            .build()
        val googleSingInClient = GoogleSignIn.getClient(context, googleSingInOptions)
        return googleSingInClient.signInIntent
    }

}