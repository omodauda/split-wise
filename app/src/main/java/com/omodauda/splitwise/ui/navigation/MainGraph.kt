package com.omodauda.splitwise.ui.navigation

import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.omodauda.splitwise.ui.components.toast.ToastHostState
import com.omodauda.splitwise.ui.features.auth.AuthViewModel
import com.omodauda.splitwise.ui.features.main.accountSettings.AccountSettingScreen
import com.omodauda.splitwise.ui.features.main.accountSettings.ChangePasswordViewModel
import com.omodauda.splitwise.ui.features.main.accountSettings.DeleteAccountViewModel
import com.omodauda.splitwise.ui.features.main.activity.ActivityViewModel
import com.omodauda.splitwise.ui.features.main.addBill.AddBillScreen
import com.omodauda.splitwise.ui.features.main.addBill.AddBillViewModel
import com.omodauda.splitwise.ui.features.main.addBillSuccess.AddBillSuccessScreen
import com.omodauda.splitwise.ui.features.main.billDetails.BillDetailsViewModel
import com.omodauda.splitwise.ui.features.main.billList.OwedBillListDetailScreen
import com.omodauda.splitwise.ui.features.main.billList.OwingBillListDetailScreen
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentScreen
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentViewModel
import com.omodauda.splitwise.ui.features.main.friends.FriendViewModel
import com.omodauda.splitwise.ui.features.main.home.BillsViewModel
import com.omodauda.splitwise.ui.features.main.invites.InviteViewModel
import com.omodauda.splitwise.ui.features.main.paymentConfirmations.PaymentConfirmationScreen
import com.omodauda.splitwise.ui.features.main.paymentConfirmations.PaymentPendingConfirmationViewModel

fun NavGraphBuilder.mainNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    toastHostState: ToastHostState,
    adaptiveInfo: WindowAdaptiveInfo
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

            val paymentPendingConfirmationVM: PaymentPendingConfirmationViewModel = hiltViewModel(parentEntry)

            val confirmPaymentViewModel: ConfirmPaymentViewModel = hiltViewModel(parentEntry)

            HomeBottomTab(
                navController = navController,
                adaptiveInfo = adaptiveInfo,
                authViewModel = authViewModel,
                inviteViewModel = inviteViewModel,
                friendViewModel = friendViewModel,
                billsViewModel = billsViewModel,
                activityViewModel = activityViewModel,
                pendingConfirmationViewModel = paymentPendingConfirmationVM,
                confirmPaymentViewModel = confirmPaymentViewModel,
                toastHostState = toastHostState
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
        composable(
            route = Screen.OwedBillListDetail.route,
            arguments = listOf(
                navArgument("billId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            val parentEntry = remember(it) { navController.getBackStackEntry("main_graph") }
            val billsViewModel: BillsViewModel = hiltViewModel(parentEntry)
            val detailsViewModel: BillDetailsViewModel = hiltViewModel()
            val currentUser by authViewModel.user.collectAsStateWithLifecycle()
            val billId = it.arguments?.getString("billId")

            OwedBillListDetailScreen(
                billsViewModel = billsViewModel,
                detailsViewModel = detailsViewModel,
                goBack = { navController.popBackStack() },
                currentUserId = currentUser?.id ?: "",
                initialBillId = billId
            )
        }
        composable(
            route = Screen.OwingBillListDetail.route,
            arguments = listOf(
                navArgument("billId") {
                    type = NavType.StringType
                    nullable = true
                    defaultValue = null
                }
            )
        ) {
            val parentEntry = remember(it) { navController.getBackStackEntry("main_graph") }
            val billsViewModel: BillsViewModel = hiltViewModel(parentEntry)
            val detailsViewModel: BillDetailsViewModel = hiltViewModel()
            val currentUser by authViewModel.user.collectAsStateWithLifecycle()
            val billId = it.arguments?.getString("billId")

            OwingBillListDetailScreen(
                billsViewModel = billsViewModel,
                detailsViewModel = detailsViewModel,
                goBack = { navController.popBackStack() },
                currentUserId = currentUser?.id ?: "",
                initialBillId = billId
            )
        }
        composable(route = Screen.PaymentConfirmation.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }
            val paymentPendingConfirmationVM: PaymentPendingConfirmationViewModel = hiltViewModel(parentEntry)
            val confirmPaymentViewModel: ConfirmPaymentViewModel = hiltViewModel(parentEntry)
            PaymentConfirmationScreen(
                goBack = {navController.popBackStack()},
                goToConfirmPayment = {paymentId ->
                    navController.navigate(Screen.ConfirmPayment.createRoute(paymentId))
                                     },
                viewModel = paymentPendingConfirmationVM,
                confirmPaymentViewModel = confirmPaymentViewModel
            )
        }
        composable(route = Screen.ConfirmPayment.route) {
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }
            val confirmPaymentViewModel: ConfirmPaymentViewModel = hiltViewModel(parentEntry)
            ConfirmPaymentScreen(
                goBack = {navController.popBackStack()},
                confirmPaymentViewModel
            )
        }
    }
}