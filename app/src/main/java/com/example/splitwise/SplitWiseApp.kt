package com.example.splitwise

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.example.splitwise.ui.navigation.authNavGraph

@Composable
fun SplitWiseApp() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "auth_graph"
        ) {
            authNavGraph(navController)
        }
    }
}