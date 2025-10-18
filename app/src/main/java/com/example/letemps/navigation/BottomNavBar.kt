package com.example.letemps.navigation

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        NavRoutes.Home,
        NavRoutes.Search,
      //  NavRoutes.Settings
    )

    NavigationBar {
        val navBackStackEntry = navController.currentBackStackEntryAsState().value
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            val icon = when (item) {
                is NavRoutes.Home -> Icons.Default.Home
                is NavRoutes.Search -> Icons.Default.Search
               // is NavRoutes.Settings -> Icons.Default.Settings
            }

            NavigationBarItem(
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                icon = { Icon(icon, contentDescription = item.route) },
                label = { Text(item.route.replaceFirstChar { it.uppercase() }) }
            )
        }
    }
}
