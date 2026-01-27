package com.example.splitwise.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController
) {
    navigation(
        startDestination = Screen.Home.route,
        route = "main_graph"
    ) {
        composable(route = Screen.Home.route) {
            HomeBottomTab(
                navController
            )
        }

    }
}