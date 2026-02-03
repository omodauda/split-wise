package com.example.splitwise.ui.navigation

sealed class Screen(val route: String) {
    object Onboarding: Screen("Onboarding")
    object Login: Screen("Login")
    object Signup: Screen("Signup")
    object ForgotPassword: Screen("ForgotPassword")

    object Home: Screen("Home")
    object Groups: Screen(route = "Groups")
    object Activity: Screen(route = "Activity")
    object Friends: Screen(route = "Friends")
    object Profile: Screen(route = "Profile")
    object AddBill: Screen(route = "AddBill")
}