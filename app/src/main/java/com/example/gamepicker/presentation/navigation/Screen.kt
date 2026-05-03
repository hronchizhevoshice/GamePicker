package com.example.gamepicker.presentation.navigation

sealed class Screen(val route: String, val title: String) {
    object Home : Screen("home", "Главная")
    object Favorites : Screen("favorites", "Избранное")
    object Details : Screen("details/{gameId}", "Детали игры")
}