package com.example.splitwise.model

import com.example.splitwise.ui.features.main.addBill.AddBillSplitMethod
import java.util.Date

data class AddBillUiState(
    val isCurrentStepValid: Boolean = false,

    val billAmount: Double = 0.00,
    val description: String = "",
    val category: String? = null,
    val date: Date? = null,

    val isGroupSplit: Boolean = false,
    val selectedGroupId: String? = null,
    val selectedFriends: List<String> = emptyList(),

    val participants: List<User> = emptyList(),
    val paidByUserId: String? = null,

    val splitMethod: AddBillSplitMethod = AddBillSplitMethod.EQUAL,
    val splitEntries: List<SplitEntryState> = emptyList()
)
