package com.example.splitwise.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise.data.network.model.LoginRequest
import com.example.splitwise.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface AuthSubmissionState {
    data object Idle: AuthSubmissionState
    data object Loading: AuthSubmissionState
    data class Error(val message: String): AuthSubmissionState
    data object Success: AuthSubmissionState
}

data class AuthUiState(
    val submissionState: AuthSubmissionState = AuthSubmissionState.Idle
)
class AuthViewModel(private val repo: AuthRepository): ViewModel() {
    val isAuthenticated = repo.isAuthenticated
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    val user = repo.user
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )


    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun login(data: LoginRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(submissionState = AuthSubmissionState.Loading) }

            val result = repo.login(data)

            result.onSuccess {
                _uiState.update { it.copy(submissionState = AuthSubmissionState.Success) }
            }.onFailure { exception ->
                _uiState.update { it.copy(submissionState = AuthSubmissionState.Error(exception.message ?: "An error occurred")) }
            }
        }
    }

    fun resetLoginSubmissionState() {
        _uiState.update { it.copy(submissionState = AuthSubmissionState.Idle) }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}