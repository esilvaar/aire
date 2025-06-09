package com.example.aire.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.compose.*
import com.example.aire.screens.*

sealed class Screen(val route: String, val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    object Home : Screen("home", "Inicio", Icons.Default.Home)
    object Screen1 : Screen("screen1", "Mapa", Icons.Default.LocationOn)
    object Screen2 : Screen("screen2", "Historial", Icons.Default.Warning)
}

@Composable
fun Nav(onToggleTheme: () -> Unit) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    Scaffold(
        topBar = {
            if (currentRoute == Screen.Home.route || currentRoute == Screen.Screen1.route) {
                Top(onToggleTheme = onToggleTheme)
            }
        },
        bottomBar = {
            Bottom(navController = navController)
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(onToggleTheme = onToggleTheme) }
            composable(Screen.Screen1.route) { Screen1(onToggleTheme = onToggleTheme) }
            composable(Screen.Screen2.route) { Screen2() }
        }
    }
}
