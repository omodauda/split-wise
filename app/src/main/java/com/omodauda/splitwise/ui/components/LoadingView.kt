package com.omodauda.splitwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

@Composable
fun LoadingView() {
    Dialog(
        onDismissRequest = { /* User cannot dismiss */ },
        properties = DialogProperties(dismissOnClickOutside = false, dismissOnBackPress = false, usePlatformDefaultWidth = false)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.2f))
        ) {
            CircularProgressIndicator()
        }
    }
}