package com.omodauda.splitwise.ui.components.toast

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay

class ToastHostState {

    var currentToast by mutableStateOf<ToastState?>(null)
        private set

    suspend fun showToast(toast: ToastState) {
        currentToast = toast
        delay(toast.duration)
        currentToast = null
    }

    fun dismiss() {
        currentToast = null
    }
}