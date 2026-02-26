package com.example.splitwise.model
sealed interface InviteSubmissionState {
    data object Idle: InviteSubmissionState
    data object Loading: InviteSubmissionState
    data class Error(val message: String) : InviteSubmissionState
    data class Success(val message: String) : InviteSubmissionState
}

data class InviteUiState(
    val email: String = "",
    val emailError: String? = null,
    val sendInviteState: InviteSubmissionState = InviteSubmissionState.Idle,
    val actionInviteState: InviteSubmissionState = InviteSubmissionState.Idle,
//    val declineInviteState: InviteSubmissionState = InviteSubmissionState.Idle
)
