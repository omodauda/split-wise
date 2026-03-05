package com.example.splitwise.ui.features.main.invites

import android.util.Patterns
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.LoadState
import androidx.paging.cachedIn
import androidx.paging.compose.LazyPagingItems
import com.example.splitwise.data.network.model.FriendInvite
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

    fun declineInvite(inviteId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(actionInviteState = InviteSubmissionState.Loading) }
            val result = repo.declineInvite(inviteId)
            result.onSuccess { response ->
                onSuccess()
                _uiState.update { it.copy(actionInviteState = InviteSubmissionState.Success(response.message)) }
            }.onFailure { exception ->
                _uiState.update { it.copy(actionInviteState = InviteSubmissionState.Error(exception.message ?: "An error occurred")) }
            }
        }
    }

    fun acceptInvite(inviteId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _uiState.update { it.copy(actionInviteState = InviteSubmissionState.Loading) }
            val result = repo.acceptInvite(inviteId)
            result.onSuccess { response ->
                onSuccess()
                _uiState.update { it.copy(actionInviteState = InviteSubmissionState.Success(response.message)) }
            }.onFailure { exception ->
                _uiState.update { it.copy(actionInviteState = InviteSubmissionState.Error(exception.message ?: "An error occurred")) }
            }
        }
    }

    fun resetActionInviteState() {
        _uiState.update { it.copy(actionInviteState = InviteSubmissionState.Idle) }
    }

    // Pending invites
    val inviteFlow = repo.getPendingInviteStream().cachedIn(viewModelScope)
    private val _showDialog = MutableStateFlow(false)
    val showDialog: StateFlow<Boolean> = _showDialog.asStateFlow()
    private var hasHandledInitialLoad = false

    fun monitorLoadState(pagingItems: LazyPagingItems<FriendInvite>) {
        viewModelScope.launch {
            // Observe the load state of the paging items
            snapshotFlow { pagingItems.loadState.refresh }
                .collect { loadState ->
                    // When the "refresh" (initial load or manual refresh()) finishes successfully
                    if (loadState is LoadState.NotLoading) {

                        // Logic: If the list is empty AND we are currently showing the dialog, close it.
                        if (pagingItems.itemCount == 0 && _showDialog.value) {
                            _showDialog.value = false
                        }

                        // Logic: If the list is NOT empty, ensure dialog shows (existing logic)
                        else if (pagingItems.itemCount > 0 && !hasHandledInitialLoad) {
                            _showDialog.value = true
                            hasHandledInitialLoad = true
                        }
                    }
                }
        }
    }


    fun onDialogDismissed() {
        _showDialog.value = false
    }

}