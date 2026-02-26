package com.example.splitwise.ui.features.main.invites

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise.data.network.model.SendFriendInviteRequest
import com.example.splitwise.data.repository.InviteRepository
import com.example.splitwise.model.InviteSubmissionState
import com.example.splitwise.model.InviteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class InviteViewModel (private val repo: InviteRepository): ViewModel() {
    private val _uiState = MutableStateFlow(InviteUiState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { currentState ->
            currentState.copy(email = email, emailError = null)
        }
    }

    fun validateEmail(): Boolean {
        val email = _uiState.value.email

        val emailError = when {
            email.isBlank() -> "Email is required"
            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> "Invalid email address"
            else -> null
        }

        _uiState.update {
            it.copy(emailError = emailError)
        }
        return emailError == null
    }

    fun sendInvite(data: SendFriendInviteRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(sendInviteState = InviteSubmissionState.Loading) }
            val result = repo.sendInvite(data)
            result.onSuccess { response ->
                _uiState.update { it.copy(sendInviteState = InviteSubmissionState.Success(response.message)) }
            }.onFailure { exception ->
                _uiState.update { it.copy(sendInviteState = InviteSubmissionState.Error(exception.message ?: "An error occurred")) }
            }
        }
    }

    fun resetSendInviteState() {
        _uiState.update { it.copy(sendInviteState = InviteSubmissionState.Idle) }
    }
}