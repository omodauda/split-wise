package com.example.splitwise.ui.features.main.addBill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.splitwise.data.network.model.AuthUserData
import com.example.splitwise.data.repository.BillsRepository
import kotlinx.coroutines.flow.StateFlow

class AddBillViewModelFactory(private val billsRepository: BillsRepository, private val currentUser: StateFlow<AuthUserData?>): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddBillViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddBillViewModel(billsRepository, currentUser) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}