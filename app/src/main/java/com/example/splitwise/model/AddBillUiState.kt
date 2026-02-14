package com.example.splitwise.model

import com.example.splitwise.ui.features.main.addBill.AddBillSplitMethod
import java.util.Date

data class AddBillUiState(
    val isCurrentStepValid: Boolean = false,

    val billAmount: Double = 0.00,
    val description: String = "",
    val category: Int? = null,
    val date: Date? = null,

    val isGroupSplit: Boolean = false,
    val selectedGroupId: String? = null,
    val selectedFriends: List<String> = emptyList(),

    val participants: List<User> = emptyList(),
    val paidByUserId: String? = null,

    val splitMethod: AddBillSplitMethod = AddBillSplitMethod.EQUAL,
    val splitEntries: List<SplitEntryState> = emptyList()
) {
    val sumOfSplitPercentage: Double
        get() = splitEntries.sumOf { it.percentage }

    val remainingPercentage: Double
        get() = (100.00 - sumOfSplitPercentage).coerceAtLeast(0.0)

    val sumOfSplitAmount: Double
        get() = splitEntries.sumOf { it.amount }

    val remainingAmount: Double
        get() = (billAmount - sumOfSplitAmount).coerceAtLeast(0.0)
}
