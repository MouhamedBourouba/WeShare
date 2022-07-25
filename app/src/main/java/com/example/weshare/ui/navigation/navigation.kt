package com.example.weshare.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.weshare.ui.BottomBarScreen
import com.example.weshare.ui.navigation.navGraphs.authNavGraph
import com.example.weshare.ui.navigation.navGraphs.HomeNavGraph

@Composable
fun Navigation(navController: NavHostController) {
    NavHost(navController = navController, startDestination = Graph.AUTHENTICATION) {
        authNavGraph(navController)
        composable(Graph.HOME) {
            BottomBarScreen()
        }
    }
}

object Graph {
    const val HOME = "home_graph"
    const val AUTHENTICATION = "authentication_graph"
}