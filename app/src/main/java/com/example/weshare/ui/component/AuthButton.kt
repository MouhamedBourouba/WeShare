package com.example.weshare.ui.component

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.weshare.R.font.rambla_regular
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.toFontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.weshare.ui.theme.spacing


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthChoseButton(
    background: Color,
    text: String,
    icon: ImageVector,
    isGoogleAuthButton: Boolean = false,
    onClickAction: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .padding(
                horizontal = MaterialTheme.spacing.medium,
                vertical = MaterialTheme.spacing.small
            ),
        onClick = { onClickAction.invoke() },
        color = background,
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.spacing.medium),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(MaterialTheme.spacing.small),
                color = Color.White,
                fontSize = 17.sp,
                fontFamily = Font(rambla_regular).toFontFamily(),

                )
            if (isGoogleAuthButton) Icon(painter = painterResource(id = com.example.weshare.R.drawable.google), contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
            else Icon(imageVector = icon, contentDescription = null, tint = Color.White)
        }
    }
}