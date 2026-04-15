package com.omodauda.splitwise.ui.navigation

import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.omodauda.splitwise.SplitWiseApplication
import com.omodauda.splitwise.ui.components.toast.ToastHostState
import com.omodauda.splitwise.ui.features.auth.AuthViewModel
import com.omodauda.splitwise.ui.features.main.accountSettings.AccountSettingScreen
import com.omodauda.splitwise.ui.features.main.accountSettings.ChangePasswordViewModel
import com.omodauda.splitwise.ui.features.main.accountSettings.ChangePasswordViewModelFactory
import com.omodauda.splitwise.ui.features.main.accountSettings.DeleteAccountViewModel
import com.omodauda.splitwise.ui.features.main.accountSettings.DeleteAccountViewModelFactory
import com.omodauda.splitwise.ui.features.main.activity.ActivityViewModel
import com.omodauda.splitwise.ui.features.main.activity.ActivityViewModelFactory
import com.omodauda.splitwise.ui.features.main.addBill.AddBillScreen
import com.omodauda.splitwise.ui.features.main.addBill.AddBillViewModel
import com.omodauda.splitwise.ui.features.main.addBill.AddBillViewModelFactory
import com.omodauda.splitwise.ui.features.main.addBillSuccess.AddBillSuccessScreen
import com.omodauda.splitwise.ui.features.main.friends.FriendViewModel
import com.omodauda.splitwise.ui.features.main.friends.FriendViewModelFactory
import com.omodauda.splitwise.ui.features.main.home.BillsViewModel
import com.omodauda.splitwise.ui.features.main.home.BillsViewModelFactory
import com.omodauda.splitwise.ui.features.main.invites.InviteViewModel
import com.omodauda.splitwise.ui.features.main.invites.InviteViewModelFactory

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

            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }

            val friendViewModel: FriendViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = FriendViewModelFactory(application.appContainer.friendRepository)
            )

            val inviteViewModel: InviteViewModel = viewModel(
                factory = InviteViewModelFactory(inviteRepository = application.appContainer.inviteRepository)
            )

            val billsViewModel: BillsViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = BillsViewModelFactory(application.appContainer.billsRepository)
            )

            val activityViewModel: ActivityViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = ActivityViewModelFactory(application.appContainer.activityRepository)
            )

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
            val application = LocalContext.current.applicationContext as SplitWiseApplication
            val currentUser by authViewModel.user.collectAsStateWithLifecycle()
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }
            val addBillViewModel: AddBillViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = AddBillViewModelFactory(billsRepository = application.appContainer.billsRepository, authViewModel.user)
            )
            val billsViewModel: BillsViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = BillsViewModelFactory(application.appContainer.billsRepository)
            )
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
                billsViewModel = billsViewModel,
                toastHostState = toastHostState,
            )
        }
        composable( route = Screen.AddBillSuccess.route){
            val parentEntry = remember(it) {
                navController.getBackStackEntry("main_graph")
            }
            val application = LocalContext.current.applicationContext as SplitWiseApplication
            val addBillViewModel: AddBillViewModel = viewModel(
                viewModelStoreOwner = parentEntry,
                factory = AddBillViewModelFactory(billsRepository = application.appContainer.billsRepository, authViewModel.user)
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
                factory = ChangePasswordViewModelFactory(application.appContainer.authRepository)
            )

            val deleteAccountViewModel: DeleteAccountViewModel = viewModel(
                factory = DeleteAccountViewModelFactory(application.appContainer.authRepository)
            )
            AccountSettingScreen(
                goBack = {navController.popBackStack()},
                changePasswordViewModel,
                deleteAccountViewModel,
                authViewModel,
                toastHostState
            )
        }
    }
}