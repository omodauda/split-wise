package com.example.splitwise.ui.features.main.accountSettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ChangePasswordViewModelFactory(): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChangePasswordViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChangePasswordViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}