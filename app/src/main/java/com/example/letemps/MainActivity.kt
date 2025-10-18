package com.example.letemps

import android.Manifest
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import com.example.letemps.navigation.BottomNavBar
import com.example.letemps.navigation.MainNavGraph
import com.example.letemps.ui.theme.LeTempsTheme
import com.example.letemps.ui.theme.ThemeMode

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val requestPermissionLauncher =
            registerForActivityResult(ActivityResultContracts.RequestPermission()) { /* handle */ }
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION)

        setContent {
            var themeMode by remember { mutableStateOf(ThemeMode.SYSTEM) }
            val navController = rememberNavController()

            LeTempsTheme(themeMode = themeMode) {
                androidx.compose.material3.Scaffold(
                    modifier = Modifier,
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    MainNavGraph(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        onThemeChange = { selectedMode ->
                            themeMode = selectedMode
                        },
                        currentTheme = themeMode
                    )
                }
            }
        }
    }
}
