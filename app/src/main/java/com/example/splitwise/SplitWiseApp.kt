package com.example.splitwise

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.splitwise.ui.features.auth.login.LoginScreen
import com.example.splitwise.ui.features.auth.signup.SignupScreen

@Composable
fun SplitWiseApp() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
    ) { innerPadding ->
        SignupScreen()
    }
}