package com.example.letemps.navigation

sealed class NavRoutes(val route: String) {
    object Home : NavRoutes("home")
    object Search : NavRoutes("search")
   // object Settings : NavRoutes("settings")
}