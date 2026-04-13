package com.omodauda.splitwise.model

import com.omodauda.splitwise.data.network.model.Friend
import com.omodauda.splitwise.ui.features.main.addBill.AddBillSplitMethod
import java.util.Date

sealed interface AddBillSubmissionState {
    data object Idle: AddBillSubmissionState
    data object Loading: AddBillSubmissionState
    data class Success(val message: String): AddBillSubmissionState
    data class Error(val message: String): AddBillSubmissionState
}
data class AddBillUiState(
    val submissionState: AddBillSubmissionState = AddBillSubmissionState.Idle,
    val isCurrentStepValid: Boolean = false,

    val billAmount: String = "",
    val description: String = "",
    val category: String? = null,
    val date: Date? = null,

    val isGroupSplit: Boolean = false,
    val selectedGroupId: String? = null,
    val selectedFriends: List<String> = emptyList(),

    val participants: List<Friend> = emptyList(),
    val paidByUserId: String? = null,

    val splitMethod: AddBillSplitMethod = AddBillSplitMethod.EQUAL,
    val splitEntries: List<SplitEntryState> = emptyList()
) {
    val billAmountAsDouble: Double
        get() = (billAmount.toDoubleOrNull() ?: 0.0) / 100.0
    val sumOfSplitPercentage: Double
        get() = splitEntries.sumOf { it.percentage }

    val remainingPercentage: Double
        get() = (100.00 - sumOfSplitPercentage).coerceAtLeast(0.0)

    val sumOfSplitAmount: Double
        get() = splitEntries.sumOf { it.amount }

    val remainingAmount: Double
        get() = (billAmountAsDouble - sumOfSplitAmount).coerceAtLeast(0.0)
}
