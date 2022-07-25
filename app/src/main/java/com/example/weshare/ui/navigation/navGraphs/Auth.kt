package com.example.weshare.ui.navigation.navGraphs

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.weshare.ui.navigation.Graph
import com.example.weshare.ui.navigation.ScreensRoutes
import com.example.weshare.ui.screens.Auth.AuthScreen
import com.example.weshare.ui.screens.complete_account.CompleteAccount

fun NavGraphBuilder.authNavGraph(navController: NavController) {
    navigation(startDestination = ScreensRoutes.Auth.route, route = Graph.AUTHENTICATION) {
        composable(ScreensRoutes.Auth.route) {
            AuthScreen(navController)
        }
        composable(ScreensRoutes.CompleteAccount.route) {
            CompleteAccount(navController)
        }
    }
}