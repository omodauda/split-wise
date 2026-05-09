package com.omodauda.splitwise.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.omodauda.splitwise.ui.components.toast.ToastHostState
import com.omodauda.splitwise.ui.features.auth.AuthViewModel
import com.omodauda.splitwise.ui.features.main.accountSettings.AccountSettingScreen
import com.omodauda.splitwise.ui.features.main.accountSettings.ChangePasswordViewModel
import com.omodauda.splitwise.ui.features.main.accountSettings.DeleteAccountViewModel
import com.omodauda.splitwise.ui.features.main.activity.ActivityViewModel
import com.omodauda.splitwise.ui.features.main.addBill.AddBillScreen
import com.omodauda.splitwise.ui.features.main.addBill.AddBillViewModel
import com.omodauda.splitwise.ui.features.main.addBillSuccess.AddBillSuccessScreen
import com.omodauda.splitwise.ui.features.main.billDetails.BillDetailsScreen
import com.omodauda.splitwise.ui.features.main.billList.OwedBillListScreen
import com.omodauda.splitwise.ui.features.main.billList.OwingBillListScreen
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentScreen
import com.omodauda.splitwise.ui.features.main.friends.FriendViewModel
import com.omodauda.splitwise.ui.features.main.home.BillsViewModel
import com.omodauda.splitwise.ui.features.main.invites.InviteViewModel
import com.omodauda.splitwise.ui.features.main.paymentConfirmations.PaymentConfirmationScreen

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    toastHostState: ToastHostState
) {
    navigation(
        startDestination = Screen.Home.route,
        route = "main_graph"
    ) {
        composable(route = Screen.Home.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }

            val friendViewModel: FriendViewModel = hiltViewModel(parentEntry)

            val inviteViewModel: InviteViewModel = hiltViewModel()

            val billsViewModel: BillsViewModel = hiltViewModel(parentEntry)

            val activityViewModel: ActivityViewModel = hiltViewModel(parentEntry)

            HomeBottomTab(
                navController,
                authViewModel,
                inviteViewModel,
                friendViewModel,
                billsViewModel,
                activityViewModel,
                toastHostState
            )
        }
        composable(route = Screen.AddBill.route){
            val currentUser by authViewModel.user.collectAsStateWithLifecycle()
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }
            val addBillViewModel: AddBillViewModel = hiltViewModel(parentEntry)
            AddBillScreen(
                rootNavController = navController,
                goBack = {
                    navController.popBackStack()
                    addBillViewModel.resetState()
                },
                goToAddBillSuccess = {
                    navController.navigate(Screen.AddBillSuccess.route)
                },
                addBillViewModel,
                currentUserId = currentUser?.id,
                toastHostState = toastHostState,
            )
        }
        composable( route = Screen.AddBillSuccess.route){
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }

            val addBillViewModel: AddBillViewModel = hiltViewModel(parentEntry)

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
        composable (route = Screen.AccountSettings.route){
            val changePasswordViewModel: ChangePasswordViewModel = hiltViewModel()

            val deleteAccountViewModel: DeleteAccountViewModel = hiltViewModel()
            AccountSettingScreen(
                goBack = {navController.popBackStack()},
                changePasswordViewModel,
                deleteAccountViewModel,
                authViewModel,
                toastHostState
            )
        }
        composable(route = Screen.OwedBillList.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }
            val billsViewModel: BillsViewModel = hiltViewModel(parentEntry)
            OwedBillListScreen(
                viewModel = billsViewModel,
                goBack = {navController.popBackStack()},
                goToBillDetails = {billId ->
                    navController.navigate(Screen.BillDetails.createRoute(billId))
                }
            )
        }
        composable(route = Screen.OwingBillList.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }
            val billsViewModel: BillsViewModel = hiltViewModel(parentEntry)
            OwingBillListScreen(
                viewModel = billsViewModel,
                goBack = {navController.popBackStack()},
                goToBillDetails = {billId ->
                    navController.navigate(Screen.BillDetails.createRoute(billId))
                }
            )
        }
        composable(route = Screen.BillDetails.route) {
            val currentUser by authViewModel.user.collectAsStateWithLifecycle()
            BillDetailsScreen(
                currentUserId = currentUser?.id ?: "",
                onBackClick = {navController.popBackStack()}
            )
        }
        composable(route = Screen.PaymentConfirmation.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }
            val billsViewModel: BillsViewModel = hiltViewModel(parentEntry)
            PaymentConfirmationScreen(
                goBack = {navController.popBackStack()},
                goToConfirmPayment = {navController.navigate(Screen.ConfirmPayment.route)},
                viewModel = billsViewModel
            )
        }
        composable(route = Screen.ConfirmPayment.route) {
            ConfirmPaymentScreen(
                goBack = {navController.popBackStack()}
            )
        }
    }
}