package com.example.splitwise.ui.navigation

import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.example.splitwise.SplitWiseApplication
import com.example.splitwise.ui.components.toast.ToastHostState
import com.example.splitwise.ui.features.auth.AuthViewModel
import com.example.splitwise.ui.features.main.accountSettings.AccountSettingScreen
import com.example.splitwise.ui.features.main.accountSettings.DeleteAccountViewModel
import com.example.splitwise.ui.features.main.addBill.AddBillScreen
import com.example.splitwise.ui.features.main.addBill.AddBillViewModel
import com.example.splitwise.ui.features.main.addBillSuccess.AddBillSuccessScreen
import com.example.splitwise.ui.features.main.accountSettings.ChangePasswordViewModel
import com.example.splitwise.ui.features.main.accountSettings.ChangePasswordViewModelFactory
import com.example.splitwise.ui.features.main.accountSettings.DeleteAccountViewModelFactory
import com.example.splitwise.ui.features.main.addBill.AddBillViewModelFactory
import com.example.splitwise.ui.features.main.invites.InviteViewModel
import com.example.splitwise.ui.features.main.invites.InviteViewModelFactory

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
            val application = LocalContext.current.applicationContext as SplitWiseApplication
            val inviteViewModel: InviteViewModel = viewModel(
                factory = InviteViewModelFactory(inviteRepository = application.appContainer.inviteRepository)
            )
            HomeBottomTab(
                navController,
                authViewModel,
                inviteViewModel,
                toastHostState
            )
        }
        composable(route = Screen.AddBill.route){
            val addBillViewModel: AddBillViewModel = viewModel(
                factory = AddBillViewModelFactory()
            )
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
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }
            val addBillViewModel: AddBillViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = AddBillViewModelFactory()
            )

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
            val application = LocalContext.current.applicationContext as SplitWiseApplication
            val changePasswordViewModel: ChangePasswordViewModel = viewModel(
                factory = ChangePasswordViewModelFactory()
            )

            val deleteAccountViewModel: DeleteAccountViewModel = viewModel(
                factory = DeleteAccountViewModelFactory(application.appContainer.authRepository)
            )
            AccountSettingScreen(
                goBack = {navController.popBackStack()},
                changePasswordViewModel,
                deleteAccountViewModel
            )
        }
    }
}