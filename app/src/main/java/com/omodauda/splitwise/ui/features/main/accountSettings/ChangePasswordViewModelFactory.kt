package com.omodauda.splitwise.ui.features.main.accountSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omodauda.splitwise.data.repository.AuthRepository

class ChangePasswordViewModelFactory(private val authRepository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChangePasswordViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}