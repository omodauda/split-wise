package com.example.splitwise

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.splitwise.ui.features.auth.AuthViewModel
import com.example.splitwise.ui.features.auth.AuthViewModelFactory
import com.example.splitwise.ui.features.main.accountSettings.ChangePasswordViewModel
import com.example.splitwise.ui.features.main.accountSettings.ChangePasswordViewModelFactory
import com.example.splitwise.ui.features.main.accountSettings.DeleteAccountViewModel
import com.example.splitwise.ui.features.main.accountSettings.DeleteAccountViewModelFactory
import com.example.splitwise.ui.features.main.addBill.AddBillViewModel
import com.example.splitwise.ui.features.main.addBill.AddBillViewModelFactory
import com.example.splitwise.ui.navigation.authNavGraph
import com.example.splitwise.ui.navigation.mainNavGraph

@Composable
fun SplitWiseApp() {
    val application = LocalContext.current.applicationContext as SplitWiseApplication
    val authRepository = application.appContainer.authRepository
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository)
    )

    val addBillViewModel: AddBillViewModel = viewModel(
        factory = AddBillViewModelFactory()
    )

    val changePasswordViewModel: ChangePasswordViewModel = viewModel(
        factory = ChangePasswordViewModelFactory()
    )

    val deleteAccountViewModel: DeleteAccountViewModel = viewModel(
        factory = DeleteAccountViewModelFactory(authRepository)
    )

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        if (isAuthenticated !== null) {
            val startDestination = if (isAuthenticated as Boolean) "main_graph" else "auth_graph"

            NavHost(
                navController,
                startDestination = startDestination
            ) {
                authNavGraph(navController, authViewModel)
                mainNavGraph(navController, authViewModel, addBillViewModel, changePasswordViewModel, deleteAccountViewModel)
            }
        }
    }
}