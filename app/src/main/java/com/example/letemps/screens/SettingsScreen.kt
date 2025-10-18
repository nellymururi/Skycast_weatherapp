package com.example.letemps.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*

import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.letemps.ui.theme.ThemeMode
import androidx.compose.ui.graphics.vector.ImageVector

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentTheme: ThemeMode,
    onThemeSelected: (ThemeMode) -> Unit
) {
    // sheet visibility state
    var showSheet by remember { mutableStateOf(false) }

    // local state for currently selected mode (reflects global selection initially)
    var selectedMode by remember { mutableStateOf(currentTheme) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Settings") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {
            // Appearance row - acts like a button to open sheet
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showSheet = true }
                    .padding(vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("Appearance", modifier = Modifier.weight(1f))
                Text(
                    when (currentTheme) {
                        ThemeMode.LIGHT -> "Light"
                        ThemeMode.DARK -> "Dark"
                        ThemeMode.SYSTEM -> "System"
                    }
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.PhoneAndroid,
                    contentDescription = "appearance"
                )
            }
        }

        // Modal Bottom Sheet
        if (showSheet) {
            ModalBottomSheet(
                onDismissRequest = { showSheet = false }
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text("Select appearance", style = MaterialTheme.typography.titleMedium)

                    Spacer(modifier = Modifier.height(12.dp))

                    // Option entries
                    ThemeOptionRow(
                        icon = Icons.Default.WbSunny,
                        title = "Light",
                        selected = selectedMode == ThemeMode.LIGHT
                    ) {
                        selectedMode = ThemeMode.LIGHT
                        onThemeSelected(ThemeMode.LIGHT)
                        showSheet = false
                    }

                    ThemeOptionRow(
                        icon = Icons.Default.DarkMode,
                        title = "Dark",
                        selected = selectedMode == ThemeMode.DARK
                    ) {
                        selectedMode = ThemeMode.DARK
                        onThemeSelected(ThemeMode.DARK)
                        showSheet = false
                    }

                    ThemeOptionRow(
                        icon = Icons.Default.PhoneAndroid,
                        title = "System default",
                        selected = selectedMode == ThemeMode.SYSTEM
                    ) {
                        selectedMode = ThemeMode.SYSTEM
                        onThemeSelected(ThemeMode.SYSTEM)
                        showSheet = false
                    }
                }
            }
        }
    }
}

@Composable
private fun ThemeOptionRow(
    icon: ImageVector,
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(icon, contentDescription = title)
        Spacer(modifier = Modifier.width(12.dp))
        Text(title, modifier = Modifier.weight(1f))
        if (selected) {
            Icon(
                imageVector = Icons.Default.Check,
                contentDescription = "selected"
            )
        }
    }
}
