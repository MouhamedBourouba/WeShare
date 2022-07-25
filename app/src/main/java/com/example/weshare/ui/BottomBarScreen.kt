package com.example.weshare.ui

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.weshare.domain.model.BottomNavItem
import com.example.weshare.ui.navigation.ScreensRoutes
import com.example.weshare.ui.navigation.navGraphs.HomeNavGraph
import com.example.weshare.ui.theme.offWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomBarScreen(navController: NavHostController = rememberNavController()) {

    val bottomBarItems = listOf(
        BottomNavItem("Home", Icons.Filled.Home, ScreensRoutes.Home.route),
        BottomNavItem("Chat", Icons.Filled.Chat, ScreensRoutes.Chat.route),
        BottomNavItem(
            "Notifications",
            Icons.Filled.Notifications,
            ScreensRoutes.Notifications.route
        ),
        BottomNavItem("Profile", Icons.Filled.Person, ScreensRoutes.Profile.route),
    )
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination

    val bottomBarDestination = bottomBarItems.any { it.route == currentDestination?.route }
    Log.d("TAG", "BottomBarScreen: ${currentDestination?.route}")

    val topBarTitle = { route: String? ->
        when (route) {
            ScreensRoutes.Profile.route -> "Your Account"
            ScreensRoutes.Chat.route -> "Chat"
            ScreensRoutes.Home.route -> "Main Feed"
            ScreensRoutes.Notifications.route -> "Notifications"
            else -> null
        }
    }
    val topBarActionIcon = { route: String? ->
        when (route) {
            ScreensRoutes.Home.route -> Icons.Filled.Search
            else -> null
        }
    }


    if (bottomBarDestination) {
        Scaffold(
            containerColor = offWhite,
            topBar = {
                TopBar(
                    title = topBarTitle(currentDestination?.route) ?: "...",
                    actionsIcon = topBarActionIcon(currentDestination?.route)
                ) {
                    navController.navigate(ScreensRoutes.Search.route)
                }
            },

            floatingActionButtonPosition = FabPosition.End,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = {
                        navController.navigate(ScreensRoutes.AddPost.route)
                    },
                    shape = CircleShape,
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = Color.White,
                ) {
                    Icon(imageVector = Icons.Filled.Add, contentDescription = null)
                }
            },
            bottomBar = {
                BottomBar(
                    bottomNavItem = bottomBarItems,
                    navController.currentDestination,
                ) {
                    navController.navigate(it.route)
                }
            }
        ) {
            Box(
                Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                HomeNavGraph(navController = navController)
            }
        }
    } else {
        HomeNavGraph(navController = navController)
    }
}


@Composable
fun TopBar(title: String, actionsIcon: ImageVector?, onSearchIconClick: () -> Unit) {
    Column {
        SmallTopAppBar(
            title = {
                Text(text = title)
            },
            modifier = Modifier,
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = Color.White,
                titleContentColor = Color.Black,
                actionIconContentColor = Color.Black
            ),
            actions = {
                if (actionsIcon != null) {
                    IconButton(
                        onClick = {
                            onSearchIconClick()
                        }) {
                        Icon(imageVector = actionsIcon, contentDescription = null)
                    }
                }
            }
        )
        Divider(thickness = 1.dp, color = Color.LightGray.copy(0.7f))
    }
}

@Composable
fun BottomBar(
    bottomNavItem: List<BottomNavItem>,
    currentDestination: NavDestination?,
    onItemClick: (BottomNavItem) -> Unit,
) {
    Column {
        Divider(thickness = 1.dp, color = Color.LightGray.copy(0.7f))
        BottomAppBar(
            containerColor = Color.White,
            tonalElevation = 6.dp,
            icons = {
                bottomNavItem.forEach { bottomNavItem ->
                    AddItem(
                        selected = currentDestination?.hierarchy?.any {
                            bottomNavItem.route == it.route
                        } == true,
                        icon = bottomNavItem.icon,
                        label = bottomNavItem.name
                    ) {
                        onItemClick(bottomNavItem)
                    }
                }
            },
            modifier = Modifier.clip(RoundedCornerShape(15.dp, 15.dp, 0.dp, 0.dp)),
        )
    }
}

@Composable
fun RowScope.AddItem(selected: Boolean, icon: ImageVector, label: String, onClick: () -> Unit) {
    NavigationBarItem(
        selected = selected,
        onClick = {
            onClick()
        },
        icon = {
            Icon(imageVector = icon, contentDescription = null)
        },
        label = {
            Text(text = label)
        },
        alwaysShowLabel = false,
        colors = NavigationBarItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            indicatorColor = Color.White,
            selectedTextColor = MaterialTheme.colorScheme.primary
        )
    )
}