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
import com.example.splitwise.ui.components.toast.ToastHost
import com.example.splitwise.ui.components.toast.rememberToastHostState
import com.example.splitwise.ui.features.auth.AuthViewModel
import com.example.splitwise.ui.features.auth.AuthViewModelFactory
import com.example.splitwise.ui.navigation.authNavGraph
import com.example.splitwise.ui.navigation.mainNavGraph

@Composable
fun SplitWiseApp() {
    val application = LocalContext.current.applicationContext as SplitWiseApplication
    val authRepository = application.appContainer.authRepository
    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModelFactory(authRepository)
    )

    val isAuthenticated by authViewModel.isAuthenticated.collectAsState()
    val navController = rememberNavController()
    val toastHostState = rememberToastHostState()


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
                authNavGraph(navController, authViewModel, toastHostState)
                mainNavGraph(navController, authViewModel, toastHostState)
            }
            ToastHost(
                hostState = toastHostState
            )
        }
    }
}