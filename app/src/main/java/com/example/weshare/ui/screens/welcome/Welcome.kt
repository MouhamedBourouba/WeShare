package com.example.weshare.ui.screens.welcome

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Facebook
import androidx.compose.material.icons.filled.Mail
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import com.example.weshare.R.drawable.logo_with_txt
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.weshare.ui.component.AuthChoseButton
import com.example.weshare.ui.screens.destinations.EmailAuthDestination
import com.example.weshare.ui.theme.WeShareTheme
import com.example.weshare.ui.theme.spacing
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

@Composable
@Destination
@RootNavGraph(start = true)
fun Welcome(navigator: DestinationsNavigator) {
    WeShareTheme {
        MainContent(navigator)
    }
}

@Composable
private fun MainContent(navigator: DestinationsNavigator) {
    Surface(
        modifier = Modifier
            .padding(MaterialTheme.spacing.extraSmall)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Image(
                modifier = Modifier
                    .size(355.dp),
                painter = painterResource(id = logo_with_txt),
                contentDescription = null
            )
            AuthChose(navigator)
            TermsAndConditions()
        }
    }
}

@Composable
private fun TermsAndConditions() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = MaterialTheme.spacing.large),
        contentAlignment = Alignment.BottomCenter
    ) {
        Text(
            text = buildAnnotatedString {
                withStyle(style = SpanStyle(color = Color.Black)) {
                    append("by Clicking “CONTINUE” above you agree to random chat ")
                }
                withStyle(style = SpanStyle(color = Color(0XFF5F6A1A))) {
                    append("terms & conditions")
                }
            },
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.medium),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun AuthChose(navigator: DestinationsNavigator) {
    Column(
        modifier = Modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AuthChoseButton(
            background = Color(0XFFDB4437),
            text = "Continue With Google",
            isGoogleAuthButton = true,
            icon = Icons.Filled.Email
        ) {

        }
        AuthChoseButton(
            background = Color(0XFF4267B2),
            text = "Continue With Facebook",
            icon = Icons.Filled.Facebook
        ) {

        }

        AuthChoseButton(
            background = MaterialTheme.colorScheme.primary,
            text = "Continue With Email",
            icon = Icons.Filled.Mail
        ) {
            navigator.navigate(EmailAuthDestination)
        }
    }
}
