package com.example.weshare.ui.screens.Auth

import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
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
import com.example.weshare.ui.theme.facebookBlue
import com.example.weshare.ui.theme.googleRed
import com.example.weshare.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Destination
@RootNavGraph(true)
@Composable
fun AuthScreen(
    navigator: DestinationsNavigator,
    viewModel: AuhViewModel = hiltViewModel()
) {
    MainContent(viewModel)
}

@Composable
@Preview
private fun MainContent(viewModel: AuhViewModel = hiltViewModel()) {
    val configuration = LocalConfiguration.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(top = 40.dp, bottom = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        TextFields(viewModel = viewModel)


        val changeAuthText = { isLogin: Boolean ->
            if (isLogin) "Don't Have Account? " else "Already Have Account? "
        }
        val loginDp = configuration.screenHeightDp / 6
        val registerDp = configuration.screenHeightDp / 11
        Log.d("dp", "MainContent: $loginDp")
        Spacer(modifier = Modifier.padding(top = if (viewModel.uiState.value.isLogin) loginDp.dp else registerDp.dp ))
        
        Text(
            modifier = Modifier
                .clickable {
                    viewModel.onEvent(AuthUiEvent.ChangeScreen)
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
private fun TextFields(viewModel: AuhViewModel) {

    Column(
        modifier = Modifier.padding(bottom = 0.dp, top = 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = null,
            modifier = Modifier
                .size(170.dp)
                .padding(top = 0.dp)
        )

        Spacer(modifier = Modifier.padding(bottom = 30.dp))


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
                viewModel.onEvent(AuthUiEvent.UserNameTextChanged(it))
            },
            placeholder = "Name", leadingIcon = Icons.Filled.Person,
        )

        MTextFiled(
            value = viewModel.uiState.value.email,
            onChangeListener = {
                viewModel.onEvent(AuthUiEvent.EmailTextChanged(it))
            },
            placeholder = "Email", leadingIcon = Icons.Filled.Email,
        )

        MTextFiled(
            value = viewModel.uiState.value.password,
            onChangeListener = {
                viewModel.onEvent(AuthUiEvent.PasswordTextChanged(it))
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
                viewModel.onEvent(AuthUiEvent.OnSingInBtnClicked)
            }
        }

        Spacer(modifier = Modifier.padding(MaterialTheme.spacing.medium))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp),
                horizontalArrangement = Arrangement.Start
            ) {
                Divider(modifier = Modifier.fillMaxWidth(0.46f))
                Text(modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small) ,text = "OR", style = MaterialTheme.typography.labelMedium)
                Divider(modifier = Modifier.fillMaxWidth(1f))
            }
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {
                    // TODO: Email Auth
                }) {
                    Icon(
                        modifier = Modifier
                            .size(30.dp),
                        painter = painterResource(id = R.drawable.google),
                        contentDescription = null,
                        tint = googleRed
                    )
                }
                IconButton(onClick = {
                    // TODO: FaceBook Auth
                }) {
                    Icon(
                        modifier = Modifier
                            .size(35.dp),
                        imageVector = Icons.Filled.Facebook,
                        contentDescription = null,
                        tint = facebookBlue
                    )
                }
            }
        }

    }
}


