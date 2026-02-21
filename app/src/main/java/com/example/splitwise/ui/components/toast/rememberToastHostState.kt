package com.example.splitwise.ui.components.toast

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember

@Composable
fun rememberToastHostState(): ToastHostState {
    return remember { ToastHostState() }
}