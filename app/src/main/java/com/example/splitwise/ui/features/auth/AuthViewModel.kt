package com.example.splitwise.ui.features.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise.data.repository.AuthRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AuthViewModel(private val repo: AuthRepository): ViewModel() {
    val isAuthenticated = repo.isAuthenticated
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = null
        )

    fun authenticate() {
        viewModelScope.launch {
            repo.authenticate()
        }
    }

    fun logout() {
        viewModelScope.launch {
            repo.logout()
        }
    }
}