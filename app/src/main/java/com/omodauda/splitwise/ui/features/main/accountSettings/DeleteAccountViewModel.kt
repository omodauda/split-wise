package com.omodauda.splitwise.ui.features.main.accountSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omodauda.splitwise.data.network.model.DeleteAccountRequest
import com.omodauda.splitwise.data.repository.AuthRepository
import com.omodauda.splitwise.ui.features.auth.AuthSubmissionState
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.onFailure

data class DeleteAccountUiState(
    val step: Int = 1,
    val deleteText: String = "",
    val password: String = "",

    val isFormValid: Boolean = true,
    val isDeleteTextError: Boolean = false,

    val submissionState: AuthSubmissionState = AuthSubmissionState.Idle
)

class DeleteAccountViewModel(private val authRepository: AuthRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(DeleteAccountUiState())
    val uiState: StateFlow<DeleteAccountUiState> = _uiState.asStateFlow()

    fun onDeleteTextChanged(text: String) {
        _uiState.update { it.copy(deleteText = text) }
        validateForm()
    }

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
        validateForm()
    }

    fun onGoToNextStep() {
        if (_uiState.value.step == 1) {
            _uiState.update { it.copy(step = 2) }
            validateForm()
        } else {
            val data = DeleteAccountRequest(password = _uiState.value.password)
            deleteAccount(data)
        }
    }

    fun onGoToPrevStep() {
        if (_uiState.value.step == 2) {
            _uiState.update { it.copy(step = 1) }
            validateForm()
        }
    }

    fun deleteAccount(data: DeleteAccountRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(submissionState = AuthSubmissionState.Loading) }
            val result = authRepository.deleteAccount(data)
            result.onSuccess {
                _uiState.update { it.copy(submissionState = AuthSubmissionState.Success) }
                delay(5_000L)
                authRepository.logout()
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
        val state = _uiState.value
        val isDeleteTextError = state.deleteText.isNotEmpty() && state.deleteText != "DELETE"
        val isFormValid = when (state.step) {
            1 -> true
            2 -> state.deleteText == "DELETE" && state.password.isNotEmpty()
            else -> false
        }
        _uiState.update {
            it.copy(
                isDeleteTextError = isDeleteTextError,
                isFormValid = isFormValid
            )
        }
    }

}