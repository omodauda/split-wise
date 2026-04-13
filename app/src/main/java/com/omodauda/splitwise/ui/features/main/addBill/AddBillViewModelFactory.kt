package com.omodauda.splitwise.ui.features.main.addBill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omodauda.splitwise.data.network.model.AuthUserData
import com.omodauda.splitwise.data.repository.BillsRepository
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