package com.example.splitwise.ui.features.main.invites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.splitwise.data.repository.InviteRepository

class InviteViewModelFactory(private val inviteRepository: InviteRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InviteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InviteViewModel(inviteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}