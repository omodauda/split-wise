package com.omodauda.splitwise.ui.components.toast

data class ToastState(
    val message: String,
    val type: ToastType,
    val actionLabel: String? = null,
    val onAction: (() -> Unit)? = null,
    val duration: Long = 3_000L
)

enum class ToastType {
    SUCCESS,
    ERROR,
    INFO
}