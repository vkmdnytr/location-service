package com.marti.map.navigation

sealed class Screen(val route: String) {
    data object Splash : Screen("splash")
    data object Home : Screen("home")
    data object Settings : Screen("settings")
    data object Profile : Screen("profile")
    data object Map : Screen("map")
} 