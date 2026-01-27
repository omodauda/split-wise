package com.example.splitwise.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding: Screen("Onboarding")
    object Login: Screen("Login")
    object Signup: Screen("Signup")
}