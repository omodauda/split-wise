package com.example.splitwise.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.splitwise.ui.features.auth.AuthViewModel
import com.example.splitwise.ui.features.main.addBill.AddBillScreen
import com.example.splitwise.ui.features.main.addBill.AddBillViewModel
import com.example.splitwise.ui.features.main.addBillSuccess.AddBillSuccessScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    addBillViewModel: AddBillViewModel
) {
    navigation(
        startDestination = Screen.Home.route,
        route = "main_graph"
    ) {
        composable(route = Screen.Home.route) {
            HomeBottomTab(
                navController,
                authViewModel
            )
        }
        composable(route = Screen.AddBill.route){
            AddBillScreen(
                goBack = {
                    navController.popBackStack()
                    addBillViewModel.resetState()
                },
                goToAddBillSuccess = {
                    navController.navigate(Screen.AddBillSuccess.route)
                },
                addBillViewModel
            )
        }
        composable( route = Screen.AddBillSuccess.route){
            AddBillSuccessScreen(
                goHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                    addBillViewModel.resetState()
                },
                addBillViewModel
            )
        }
    }
}