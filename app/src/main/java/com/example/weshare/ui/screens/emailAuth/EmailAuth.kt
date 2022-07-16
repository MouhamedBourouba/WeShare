package com.example.weshare.ui.screens.emailAuth

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.weshare.R
import com.example.weshare.ui.component.MButton
import com.example.weshare.ui.component.MTextFiled
import com.example.weshare.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@Composable
fun EmailAuth(
    navigator: DestinationsNavigator,
    viewModel: EmailAuhViewModel = hiltViewModel()
) {
    MainContent(viewModel)
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
@Preview
private fun MainContent(viewModel: EmailAuhViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(top = 16.dp)
        )

        TextFields(viewModel = viewModel)


        val changeAuthText = { isLogin: Boolean ->
            if (isLogin) "Don't Have Account? " else "Already Have Account? "
        }
        Text(
            modifier = Modifier
                .clickable {
                    viewModel.onEvent(EmailAuthUiEvent.ChangeScreen)
                },
            textAlign = TextAlign.Center,
            text = buildAnnotatedString {
                withStyle(
                    style = SpanStyle(
                        color = Color.Black
                    )
                ) {
                    append(changeAuthText(viewModel.uiState.value.isLogin))
                }
                withStyle(
                    style = SpanStyle(
                        color = Color(0XFF5F6A1A)
                    ),
                ) {
                    append(if (viewModel.uiState.value.isLogin) "Create Account!" else "Login now!")
                }
            }
        )
    }
    ///////////////////////////////////////////////////////////////////////////////////
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun TextFields(viewModel: EmailAuhViewModel) {
    Column(modifier = Modifier.padding(bottom = 120.dp, top = 40.dp)) {

        Text(
            text = if (viewModel.uiState.value.isLogin) "Login: " else "Register: ",
            fontSize = 30.sp,
            modifier = Modifier
                .align(Alignment.Start)
                .padding(start = MaterialTheme.spacing.medium)
        )

        Spacer(modifier = Modifier.padding(bottom = MaterialTheme.spacing.small))

        if (!viewModel.uiState.value.isLogin) MTextFiled(
            value = viewModel.uiState.value.userName,
            onChangeListener = {
                viewModel.onEvent(EmailAuthUiEvent.UserNameTextChanged(it))
            },
            placeholder = "User Name", leadingIcon = Icons.Filled.Person,
        )

        MTextFiled(
            value = viewModel.uiState.value.email,
            onChangeListener = {
                viewModel.onEvent(EmailAuthUiEvent.EmailTextChanged(it))
            },
            placeholder = "Email", leadingIcon = Icons.Filled.Email,
        )

        MTextFiled(
            value = viewModel.uiState.value.password,
            onChangeListener = {
                viewModel.onEvent(EmailAuthUiEvent.PasswordTextChanged(it))
            },
            placeholder = "Password",
            leadingIcon = Icons.Filled.Lock,
            isPasswordTextFieldValue = true,
        )

        Spacer(modifier = Modifier.padding(top = MaterialTheme.spacing.small))

        Box(modifier = Modifier.align(Alignment.End)) {
            MButton(
                buttonText = "Sing ${if (viewModel.uiState.value.isLogin) "In" else "Up"}",
                isEnabled = if (viewModel.uiState.value.isLogin) viewModel.uiState.value.isSingInEnable else viewModel.uiState.value.isSingUpEnable
            ) {
                viewModel.onEvent(EmailAuthUiEvent.OnSingInBtnClicked)
            }
        }

    }
}


