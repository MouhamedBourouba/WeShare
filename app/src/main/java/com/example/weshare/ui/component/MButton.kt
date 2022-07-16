package com.example.weshare.ui.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun MButton(
    buttonText: String,
    isEnabled: Boolean,
    onClick: () -> Unit,
) {
    Button(
        modifier = Modifier
            .width(140.dp)
            .padding(end = 15.dp),
        onClick = { onClick.invoke() },
        colors = ButtonDefaults.buttonColors(
            contentColor = Color.White,
//                disabledBackgroundColor = MaterialTheme.colors.primary.copy(0.4f),
//                disabledContentColor = MaterialTheme.colors.background.copy(0.5f)
        ),
        enabled = isEnabled
    ) {
        Text(
            text = buttonText,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
        )
    }
}