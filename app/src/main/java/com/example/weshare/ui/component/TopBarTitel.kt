package com.example.weshare.ui.component

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun TopBarTitle(title: String) {
    Text(text = title, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth())
}