package com.example.splitwise.ui.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.splitwise.data.repository.BillsRepository

class BillsViewModelFactory(private val repo: BillsRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(BillsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return BillsViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}