package com.omodauda.splitwise.ui.features.main.accountSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omodauda.splitwise.data.network.model.ChangePasswordRequest
import com.omodauda.splitwise.data.repository.AuthRepository
import com.omodauda.splitwise.model.ChangePasswordUiState
import com.omodauda.splitwise.ui.features.auth.AuthSubmissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChangePasswordViewModel(private val repo: AuthRepository): ViewModel() {
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
            val state = uiState.value
            _uiState.update { it.copy(submissionState = AuthSubmissionState.Loading) }
            val data = ChangePasswordRequest(
                currentPassword = state.currentPassword,
                newPassword = state.newPassword
            )
            val result = repo.changePassword(data)
            result.onSuccess {
                _uiState.update { it.copy(submissionState = AuthSubmissionState.Success) }
                delay(5000L)
                resetState()
            }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            submissionState = AuthSubmissionState.Error(
                                exception.message ?: "An error occurred"
                            )
                        )
                    }
                }
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

    fun clearError() {
        _uiState.update { it.copy(submissionState = AuthSubmissionState.Idle) }
    }
}