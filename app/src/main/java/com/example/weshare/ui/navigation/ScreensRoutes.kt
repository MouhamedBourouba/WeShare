package com.example.weshare.ui.navigation

sealed class ScreensRoutes(val route: String) {
    object Auth: ScreensRoutes("auth")
    object CompleteAccount: ScreensRoutes("complete_account")
    object Home: ScreensRoutes("home")
    object Chat: ScreensRoutes("chat")
    object Notifications: ScreensRoutes("notifications")
    object Profile: ScreensRoutes("profile")
    object Search: ScreensRoutes("search")
    object AddPost: ScreensRoutes("add_post")
}
