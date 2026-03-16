package com.example.splitwise.ui.features.main.addBill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.splitwise.data.repository.AuthRepository
import com.example.splitwise.data.repository.BillRepository

class AddBillViewModelFactory(private val billRepository: BillRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBillViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddBillViewModel(billRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}