package com.example.splitwise.ui.features.auth.login

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.splitwise.model.LoginFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LoginViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(LoginFormState())
    val uiState: StateFlow<LoginFormState> = _uiState.asStateFlow()

    fun onEmailChanged(newValue: String) {
        _uiState.update { it.copy(email = newValue, emailError = null) }
    }

    fun onPasswordChanged(newValue: String) {
        _uiState.update { it.copy(password = newValue, passwordError = null) }
    }

    fun validateForm(): Boolean {
        val email = _uiState.value.email
        val password = _uiState.value.password

        val emailError = when {
            email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email address"
            else -> null
        }

        val passwordError = when {
            password.isBlank() -> "Password is required"
//            password.length < 6 -> "Password must be at least 6 characters"
            else -> null
        }

        _uiState.update {
            it.copy(emailError = emailError, passwordError = passwordError)
        }

        return emailError == null && passwordError == null
    }
}