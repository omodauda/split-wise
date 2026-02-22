package com.example.splitwise.ui.components.toast

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun ToastHost(
    hostState: ToastHostState,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize().systemBarsPadding(),
        contentAlignment = Alignment.TopCenter
    ) {
        val toast = hostState.currentToast

        AnimatedVisibility(
            visible = toast != null,
            enter = fadeIn() + slideInVertically(initialOffsetY = { -it }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = {-it})
        ) {
            toast?.let {
                AppToast(
                    toast = it,
                    onDismiss = { hostState.dismiss() }
                )
            }
        }
    }
}