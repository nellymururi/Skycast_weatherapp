package com.example.letemps.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.letemps.screens.HomeScreen
import com.example.letemps.screens.SearchScreen
import com.example.letemps.screens.SettingsScreen
import com.example.letemps.ui.theme.ThemeMode

@Composable
fun MainNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    onThemeChange: (ThemeMode) -> Unit,
    currentTheme: ThemeMode
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Home.route,
        modifier = modifier
    ) {
        composable(NavRoutes.Home.route) { HomeScreen() }
        composable(NavRoutes.Search.route) { SearchScreen() }
       // composable(NavRoutes.Settings.route) {
//            SettingsScreen(
//                currentTheme = currentTheme,
//                onThemeSelected = { mode -> onThemeChange(mode) }
//            )
        }
    }

