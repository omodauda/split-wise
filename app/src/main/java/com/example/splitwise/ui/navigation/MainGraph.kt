package com.example.splitwise.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.splitwise.ui.features.main.addBill.AddBillScreen

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
        composable(route = Screen.AddBill.route){
            AddBillScreen(goBack = {navController.popBackStack()})
        }

    }
}