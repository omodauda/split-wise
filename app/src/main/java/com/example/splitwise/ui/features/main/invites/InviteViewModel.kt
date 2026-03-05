package com.example.splitwise.ui.features.main.invites

import android.util.Patterns
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import com.example.splitwise.data.network.model.SendFriendInviteRequest
import com.example.splitwise.data.repository.InviteRepository
import com.example.splitwise.model.InviteSubmissionState
import com.example.splitwise.model.InviteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
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

    // Pending invites
    val inviteFlow = repo.getPendingInviteStream().cachedIn(viewModelScope)
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    private var hasHandledInitialLoad = false
    fun monitorLoadState(pagingItems: LazyPagingItems<*>) {
        if (hasHandledInitialLoad) return
        viewModelScope.launch {
            // snapshotFlow converts Compose State (loadState) into a Kotlin Flow
            snapshotFlow { pagingItems.loadState.refresh }
                .collect { loadState ->
                    if (loadState is LoadState.NotLoading && !hasHandledInitialLoad) {
                        if (pagingItems.itemCount > 0) {
                            _showDialog.value = true
                        }
                        // Even if count is 0, we mark as handled so it doesn't
                        // flash later if the list updates
                        hasHandledInitialLoad = true
                    }
                }
        }
    }

    fun onDialogDismissed() {
        _showDialog.value = false
    }

}