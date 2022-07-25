package com.example.weshare.ui.navigation.navGraphs

import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weshare.ui.navigation.Graph
import com.example.weshare.ui.navigation.ScreensRoutes
import com.example.weshare.ui.screens.chat.Chat
import com.example.weshare.ui.screens.home.Home
import com.example.weshare.ui.screens.notification.Notification
import com.example.weshare.ui.screens.post.AddPost
import com.example.weshare.ui.screens.profile.Profile
import com.example.weshare.ui.screens.search.Search

@Composable
fun HomeNavGraph(navController: NavController) {
    NavHost(
        navController = navController as NavHostController,
        startDestination = ScreensRoutes.Home.route,
        route = Graph.HOME
    ) {
        composable(ScreensRoutes.Home.route) {
            Home(navController)
        }
        composable(ScreensRoutes.Profile.route) {
            Profile(navController)
        }
        composable(ScreensRoutes.Chat.route) {
            Chat(navController)
        }
        composable(ScreensRoutes.Notifications.route) {
            Notification(navController)
        }
        composable(ScreensRoutes.Search.route) {
            Search(navController)
        }
        composable(ScreensRoutes.AddPost.route) {
            AddPost()
        }
    }
}
