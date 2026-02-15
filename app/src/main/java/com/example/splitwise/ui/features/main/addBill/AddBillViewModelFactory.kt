package com.example.splitwise.ui.features.main.addBill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class AddBillViewModelFactory: ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBillViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddBillViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}