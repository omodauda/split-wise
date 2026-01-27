package com.example.splitwise.ui.features.auth.forgotPassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import com.example.splitwise.model.ForgotPasswordFormState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ForgotPasswordViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(ForgotPasswordFormState())
    val uiState: StateFlow<ForgotPasswordFormState> = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.value = _uiState.value.copy(email = email, emailError = null)
    }

    fun validateForm(): Boolean {
        val email = _uiState.value.email

        val emailError = when {
            email.isEmpty() -> "Email cannot be empty"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email address"
            else -> null
        }

        _uiState.value = _uiState.value.copy(emailError = emailError)
        return emailError == null
    }
}