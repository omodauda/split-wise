package com.example.splitwise.ui.features.auth.signup

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.splitwise.model.SignupFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class SignupViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(SignupFormState())
    val uiState: StateFlow<SignupFormState> = _uiState.asStateFlow()

    fun onFullNameChanged(newValue: String) {
        _uiState.value = _uiState.value.copy(fullName = newValue, fullNameError = null)
    }

    fun onEmailChanged(newValue: String) {
        _uiState.update { it.copy(email = newValue, emailError = null) }
    }

    fun onPasswordChanged(newValue: String) {
        _uiState.update { it.copy(password = newValue, passwordError = null) }
    }

    fun onConfirmPasswordChanged(newValue: String) {
        _uiState.update { it.copy(confirmPassword = newValue, confirmPasswordError = null) }
    }

    fun validateForm(): Boolean {
        val fullName = _uiState.value.fullName
        val email = _uiState.value.email
        val password = _uiState.value.password
        val confirmPassword = _uiState.value.confirmPassword

        val fullNameError = when {
            fullName.isBlank() -> "Full name is required"
            else -> null
        }

        val emailError = when {
            email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email address"
            else -> null
        }

        val passwordError = when {
            password.isBlank() -> "Password is required"
            password.length < 8 -> "Password must be at least 8 characters"
            else -> null
        }

        val confirmPasswordError = when {
            confirmPassword.isBlank() -> "Confirm password is required"
            confirmPassword != password -> "Passwords do not match"
            else -> null
        }

        _uiState.update {
            it.copy(fullNameError = fullNameError, emailError = emailError, passwordError = passwordError, confirmPasswordError = confirmPasswordError)
        }

        return fullNameError == null &&  emailError == null && passwordError == null && confirmPasswordError == null
    }
}