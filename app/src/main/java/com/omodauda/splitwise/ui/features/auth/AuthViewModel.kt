package com.omodauda.splitwise.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omodauda.splitwise.data.network.model.GoogleAuthRequest
import com.omodauda.splitwise.data.network.model.LoginRequest
import com.omodauda.splitwise.data.network.model.SignupRequest
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenRequest
import com.omodauda.splitwise.data.network.model.UpdateProfileRequest
import com.omodauda.splitwise.data.repository.AuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface AuthSubmissionState {
    data object Idle : AuthSubmissionState
    data object Loading : AuthSubmissionState
    data class Error(val message: String) : AuthSubmissionState
    data object Success : AuthSubmissionState
}
data class LoginUiState(
    val submissionState: AuthSubmissionState = AuthSubmissionState.Idle
)
data class SignupUiState(
    val submissionState: AuthSubmissionState = AuthSubmissionState.Idle
)

data class GoogleAuthUiState(
    val submissionState: AuthSubmissionState = AuthSubmissionState.Idle
)

@HiltViewModel
class AuthViewModel @Inject constructor(private val repo: AuthRepository) : ViewModel() {
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

    private val _signupUiState = MutableStateFlow(SignupUiState())
    val signupUiState = _signupUiState.asStateFlow()

    private val _googleAuthUiState = MutableStateFlow(GoogleAuthUiState())
    val googleAuthUiState = _googleAuthUiState.asStateFlow()

    private val _loginUiState = MutableStateFlow(LoginUiState())
    val loginUiState = _loginUiState.asStateFlow()

    fun signup(data: SignupRequest) {
        viewModelScope.launch {
            _signupUiState.update { it.copy(submissionState = AuthSubmissionState.Loading) }
            val result = repo.signup(data)
            result.onSuccess {
                _signupUiState.update { it.copy(submissionState = AuthSubmissionState.Success) }
            }.onFailure { exception ->
                _signupUiState.update {
                    it.copy(
                        submissionState = AuthSubmissionState.Error(
                            exception.message ?: "An error occurred"
                        )
                    )
                }
            }
        }
    }

    fun continueWithGoogle(data: GoogleAuthRequest) {
        viewModelScope.launch {
            _googleAuthUiState.update { it.copy(submissionState = AuthSubmissionState.Loading) }
            val result = repo.googleAuth(data)
            result.onSuccess {
                _googleAuthUiState.update { it.copy(submissionState = AuthSubmissionState.Success) }
            }.onFailure { exception ->
                _googleAuthUiState.update {
                    it.copy(
                        submissionState = AuthSubmissionState.Error(
                            exception.message ?: "An error occurred"
                        )
                    )
                }
            }
        }
    }

    fun login(data: LoginRequest) {
        viewModelScope.launch {
            _loginUiState.update { it.copy(submissionState = AuthSubmissionState.Loading) }
            val result = repo.login(data)
            result.onSuccess {
                _loginUiState.update { it.copy(submissionState = AuthSubmissionState.Success) }
            }.onFailure { exception ->
                _loginUiState.update {
                    it.copy(
                        submissionState = AuthSubmissionState.Error(
                            exception.message ?: "An error occurred"
                        )
                    )
                }
            }
        }
    }

    private val _profileUiState = MutableStateFlow(LoginUiState())
    val profileUiState = _profileUiState.asStateFlow()

    fun updateProfile(data: UpdateProfileRequest) {
        viewModelScope.launch {
            _profileUiState.update { it.copy(submissionState = AuthSubmissionState.Loading) }
            val result = repo.updateProfile(data)
            result.onSuccess {
                _profileUiState.update { it.copy(submissionState = AuthSubmissionState.Success) }
            }
                .onFailure { exception ->
                    _profileUiState.update {
                        it.copy(
                            submissionState = AuthSubmissionState.Error(
                                exception.message ?: "An error occurred"
                            )
                        )
                    }
                }
        }
    }

    fun updateFcmToken(data: UpdateFcmTokenRequest) {
        viewModelScope.launch {
           repo.updateFcmToken(data)
        }
    }

    fun resetSignupState() {
        _signupUiState.update { it.copy(submissionState = AuthSubmissionState.Idle) }
    }

    fun resetGoogleAuthState() {
        _googleAuthUiState.update { it.copy(submissionState = AuthSubmissionState.Idle) }
    }

    fun resetLoginSubmissionState() {
        _loginUiState.update { it.copy(submissionState = AuthSubmissionState.Idle) }
    }

    fun resetProfileSubmissionState() {
        _profileUiState.update { it.copy(submissionState = AuthSubmissionState.Idle) }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout(this)
        }
    }
}