package com.example.weshare.domain.model

import androidx.compose.ui.graphics.vector.ImageVector

data class BottomNavItem(
    val name: String,
    val icon: ImageVector,
    val route: String,
    val badgeCounter: Int? = null
)