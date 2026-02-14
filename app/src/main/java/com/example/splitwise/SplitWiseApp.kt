package com.example.splitwise

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.splitwise.ui.features.auth.AuthViewModel
import com.example.splitwise.ui.features.auth.AuthViewModelFactory
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

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val navController = rememberNavController()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        if (isAuthenticated == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            val startDestination = if (isAuthenticated as Boolean) "main_graph" else "auth_graph"

            NavHost(
                navController,
                startDestination = startDestination
            ) {
                authNavGraph(navController, authViewModel)
                mainNavGraph(navController, authViewModel, addBillViewModel)
            }
        }
    }
}