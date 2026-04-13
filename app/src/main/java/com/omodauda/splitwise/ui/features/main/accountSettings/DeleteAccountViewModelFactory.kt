package com.omodauda.splitwise.ui.features.main.accountSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omodauda.splitwise.data.repository.AuthRepository

class DeleteAccountViewModelFactory(private val authRepository: AuthRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DeleteAccountViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DeleteAccountViewModel(authRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}