package com.example.splitwise.ui.features.main.accountSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise.model.ChangePasswordUiState
import com.example.splitwise.model.SubmissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ChangePasswordUiState())
    val uiState: StateFlow<ChangePasswordUiState> = _uiState.asStateFlow()

    fun onCurrentPasswordChange(password: String) {
        _uiState.update {
            it.copy(currentPassword = password)
        }
        validateForm()
    }

    fun onNewPasswordChanged(password: String) {
        _uiState.update {
            it.copy(
                newPassword = password,
                hasMinChars = password.length >= 8,
                hasUppercase = password.any { char -> char.isUpperCase() },
                hasLowercase = password.any { char -> char.isLowerCase() },
                hasNumber = password.any { char -> char.isDigit() },
                hasSpecialChar = password.any {char -> !char.isLetterOrDigit() },
            )
        }
        validateForm()
    }

    fun onConfirmNewPasswordChange(password: String) {
        _uiState.update { it.copy(confirmNewPassword = password) }
        validateForm()
    }

    fun changePassword() {
        if (!isFormValid()) return

        viewModelScope.launch {
            _uiState.update { it.copy(submissionState = SubmissionState.Loading) }
            delay(5_000L)
            _uiState.update { it.copy(submissionState = SubmissionState.Success) }
            delay(3_000L)
            resetState()
        }
    }

    private fun validateForm() {
        _uiState.update { it.copy(isFormValid = isFormValid()) }
    }

    private fun isFormValid(): Boolean {
        val state = _uiState.value
        return state.currentPassword.isNotBlank() &&
                state.hasMinChars &&
                state.hasUppercase &&
                state.hasLowercase &&
                state.hasNumber &&
                state.hasSpecialChar &&
                state.newPasswordMatch
    }

    fun resetState() {
        _uiState.update { ChangePasswordUiState() }
    }
}