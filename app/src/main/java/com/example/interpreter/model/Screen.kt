package com.example.interpreter.model

sealed class Screen(val route: String, val label: String) {
    object Main : Screen("main", "Main")
    object Run : Screen("run", "Run")
    object Settings : Screen("settings", "Settings")
    object Splash : Screen("splash", "Splash")
}