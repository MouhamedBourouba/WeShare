package com.example.weshare.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.weshare.ui.theme.WeShareTheme
import com.example.weshare.ui.theme.spacing

@Composable
fun Loading(loadingText: String) {
    WeShareTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.LightGray.copy(alpha = 0.4f)),
            contentAlignment = Alignment.Center
        ) {

            Surface(
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
                    .height(90.dp),
            ) {
                Row(
                    modifier = Modifier
                        .padding(start = MaterialTheme.spacing.medium),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    CircularProgressIndicator()
                    Spacer(modifier = Modifier.padding(start = MaterialTheme.spacing.medium))
                    Text(text = loadingText, style = MaterialTheme.typography.labelLarge)
                }
            }


        }
    }
}