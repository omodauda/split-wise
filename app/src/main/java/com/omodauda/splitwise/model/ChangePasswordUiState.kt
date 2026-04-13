package com.omodauda.splitwise.model

import com.omodauda.splitwise.ui.features.auth.AuthSubmissionState

data class ChangePasswordUiState(
    val currentPassword: String = "",
    val newPassword: String = "",
    val confirmNewPassword: String = "",

    val hasMinChars: Boolean = false,
    val hasUppercase: Boolean = false,
    val hasLowercase: Boolean = false,
    val hasNumber: Boolean = false,
    val hasSpecialChar: Boolean = false,

    val isFormValid: Boolean = false,

    val submissionState: AuthSubmissionState = AuthSubmissionState.Idle
) {
    val newPasswordMatch: Boolean
        get() = newPassword == confirmNewPassword
}

enum class SubmissionState {
    Idle,
    Loading,
    Success,
    Error
}
