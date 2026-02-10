package com.example.splitwise.ui.features.main.addBill.components.stepTwo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.splitwise.ui.features.main.addBill.components.stepTwo.components.Friends
import com.example.splitwise.ui.features.main.addBill.components.stepTwo.components.Groups

@Composable
fun StepTwoNavHost(
    navController: NavHostController,
    startDestination: StepTwoDestination = StepTwoDestination.GROUPS,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController,
        startDestination = startDestination.route,
        modifier
    ) {
        composable(StepTwoDestination.GROUPS.route) {
            Groups()
        }
        composable(StepTwoDestination.FRIENDS.route) {
            Friends()
        }
    }
}