package com.example.splitwise.ui.features.main.addBill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise.data.network.model.AuthUserData
import com.example.splitwise.data.network.model.BillSplit
import com.example.splitwise.data.network.model.CreateBillRequest
import com.example.splitwise.data.network.model.Friend
import com.example.splitwise.data.repository.BillsRepository
import com.example.splitwise.model.AddBillSubmissionState
import com.example.splitwise.model.AddBillUiState
import com.example.splitwise.model.SplitEntryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.math.absoluteValue
import kotlin.math.roundToInt

class AddBillViewModel(
    private val repo: BillsRepository,
    private val userFlow: StateFlow<AuthUserData?>,
): ViewModel() {
    private val _uiState = MutableStateFlow(AddBillUiState())
    val uiState: StateFlow<AddBillUiState> = _uiState.asStateFlow()

    private var currentUser: Friend? = null

    init {
        viewModelScope.launch {
            userFlow.collect { user ->
                if (user !== null) {
                    val me = Friend (
                        friendshipId = user.id,
                        userId = user.id,
                        fullName = user.fullName,
                        email = user.email,
                        avatar = user.avatar
                    )
                    if (uiState.value.participants.isEmpty()) {
                        currentUser = me
                        _uiState.update {
                            it.copy(
                                participants = listOf(me)
                            )
                        }
                    }

                }
            }
        }
    }

    fun resetState() {
        _uiState.update {
            AddBillUiState(
                participants = currentUser?.let { listOf(it) } ?: emptyList(),
                splitEntries = currentUser?.let { listOf(SplitEntryState(it)) } ?: emptyList()
            )
        }
    }
    
    fun resetSubmissionState() {
        _uiState.update { it.copy(submissionState = AddBillSubmissionState.Idle) }
    }
    fun onBillAmountChange(amount: String) {
        val filteredAmount = amount.filter { it.isDigit() }
        _uiState.update { it.copy(billAmount = filteredAmount) }
        // when amount changes, re-calc splits
        recalculateSplits()
        validateStep((1))

    }
    fun onDescriptionChange(description: String) {
        _uiState.update { it.copy(description = description) }
        validateStep((1))
    }
    fun onCategoryChange(category: String) {
        _uiState.update { it.copy(category = category) }
        validateStep((1))
    }
    fun onDateChange(date: Date) {
        _uiState.update { it.copy(date = date) }
        validateStep((1))
    }
    fun onGroupSelected(groupId: String) {
        _uiState.update {
            it.copy(
                isGroupSplit = true,
                selectedGroupId = groupId,
                selectedFriends = emptyList(),
                // when switching to a group, clear participants
                participants = emptyList(),
                splitEntries = emptyList()
            )
        }
        validateStep((2))
        // TODO: set group members as participants
    }
    fun onGroupMemberSelected(member: Friend) {
        _uiState.update { currentState ->
            val updatedParticipants = currentState.participants.toMutableList()
            val isAlreadySelected = updatedParticipants.any { it.userId == member.userId }

            if (isAlreadySelected) {
                updatedParticipants.removeAll { it.userId == member.userId }
            } else {
                updatedParticipants.add(member)
            }
            // Also update the split entries to match the new participant list
            val newEntries = updatedParticipants.map { user ->
                currentState.splitEntries.find { it.user.userId == user.userId } ?: SplitEntryState(user)
            }

            currentState.copy(
                participants = updatedParticipants,
                splitEntries = newEntries
            )
        }
        recalculateSplits()
         validateStep(3)
    }
    fun onFriendSelected(friend: Friend) {
        _uiState.update { currentState ->
            // TODO: current user should be auto included as first participants
            val updatedFriends = currentState.selectedFriends.toMutableList()
            val updatedParticipants = currentState.participants.toMutableList()

            currentUser?.let { me ->
                if (updatedParticipants.none {it.userId == me.userId}) {
                    updatedParticipants.add(0, me)
                }
             }

            val isAlreadySelected = updatedFriends.contains(friend.userId)
            if (isAlreadySelected) {
                updatedFriends.remove(friend.userId)
                updatedParticipants.removeAll { it.userId == friend.userId }
            } else {
                updatedFriends.add(friend.userId)
                updatedParticipants.add(friend)
            }

            val newEntries = updatedParticipants.map { user ->
                currentState.splitEntries.find { it.user.userId == user.userId } ?: SplitEntryState(user)
            }

            currentState.copy(
                isGroupSplit = false,
                selectedGroupId = null,
                selectedFriends = updatedFriends,
                participants = updatedParticipants,
                splitEntries = newEntries
            )
        }
        recalculateSplits()
        validateStep((2))
    }

    fun clearParticipants() {
        _uiState.update { it ->
            it.copy(
                isGroupSplit = false,
                selectedGroupId = null,
                selectedFriends = emptyList(),
                participants = currentUser?.let { listOf(it) } ?: emptyList(),
                paidByUserId = null,
                splitEntries = emptyList()
            )
        }
        validateStep(2)
    }
    fun onPayerSelected(userId: String) {
        _uiState.update { it.copy(paidByUserId = userId) }
        validateStep(4)
    }
    fun onSplitMethodChanged(method: AddBillSplitMethod) {
        _uiState.update { it.copy(splitMethod = method) }
        // When the method changes, apply the default logic for it
        when (method) {
            AddBillSplitMethod.EQUAL -> splitEqually()
            AddBillSplitMethod.PERCENTAGE, AddBillSplitMethod.EXACT -> clearSplitValues()
        }
        validateStep(5)
    }
    fun onPercentageChanged(userId: String, newPercentage: String) {
        val percentage = newPercentage.toDoubleOrNull() ?: 0.00
        val amount = (_uiState.value.billAmountAsDouble * percentage) / 100.0
        updateSplitEntry(userId, amount, percentage)
        validateStep(6)
    }
    fun onExactAmountChanged(userId: String, newAmount: String) {
        val amount = newAmount.toDoubleOrNull() ?: 0.00
        val percentage = if (_uiState.value.billAmountAsDouble > 0) (amount / _uiState.value.billAmountAsDouble) * 100 else 0.00
        updateSplitEntry(userId, amount, percentage)
        validateStep(6)
    }
    fun splitEqually() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.participants.isEmpty()) return@launch

            val equalAmount = state.billAmountAsDouble / state.participants.size
            val equalPercentage = if (state.billAmountAsDouble > 0) (equalAmount / state.billAmountAsDouble) * 100 else 0.0

            _uiState.update {
                it.copy(
                    splitEntries = it.participants.map { user ->
                        SplitEntryState(user, equalPercentage, equalAmount)
                    }
                )
            }
        }
    }
    fun distributeEvenly() {
        when (_uiState.value.splitMethod) {
            AddBillSplitMethod.PERCENTAGE -> {
                val state = _uiState.value
                if (state.participants.isEmpty()) return
                val evenPercentage = 100.0 / state.participants.size
                val evenAmount = state.billAmountAsDouble / state.participants.size
                updateAllSplitEntries(evenAmount, evenPercentage)
            }
            AddBillSplitMethod.EXACT -> {
                val state = _uiState.value
                if (state.participants.isEmpty()) return
                val evenAmount = state.billAmountAsDouble / state.participants.size
                val evenPercentage = if (state.billAmountAsDouble > 0) (evenAmount / state.billAmountAsDouble) * 100 else 0.0
                updateAllSplitEntries(evenAmount, evenPercentage)
            }
            AddBillSplitMethod.EQUAL -> {
                // Already handled by splitEqually()
            }
        }
        validateStep((6))
    }
    private fun updateSplitEntry(userId: String, newAmount: Double, newPercentage: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                splitEntries = currentState.splitEntries.map { entry ->
                    if (entry.user.userId == userId) {
                        entry.copy(amount = newAmount, percentage = newPercentage)
                    } else {
                        entry
                    }
                }
            )
        }
    }
    private fun updateAllSplitEntries(newAmount: Double, newPercentage: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                splitEntries = currentState.splitEntries.map { entry ->
                    entry.copy(amount = newAmount, percentage = newPercentage)
                }
            )
        }
    }
    private fun clearSplitValues() {
        updateAllSplitEntries(0.0, 0.0)
    }
    private fun recalculateSplits() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.participants.isEmpty()) return@launch

            val newEntries = when (state.splitMethod) {
                // If the method is EQUAL, re-distribute the new total bill amount equally.
                AddBillSplitMethod.EQUAL -> {
                    val equalAmount = state.billAmountAsDouble / state.participants.size
                    val equalPercentage = if (state.billAmountAsDouble > 0) (equalAmount / state.billAmountAsDouble) * 100.0 else 0.0
                    state.splitEntries.map { it.copy(amount = equalAmount, percentage = equalPercentage) }
                }

                // If the method is PERCENTAGE, keep the percentages and re-calculate the amounts.
                AddBillSplitMethod.PERCENTAGE -> {
                    state.splitEntries.map { entry ->
                        val newAmount = (state.billAmountAsDouble * entry.percentage) / 100.0
                        entry.copy(amount = newAmount)
                    }
                }

                // If the method is EXACT, the amounts are fixed by the user, so we just
                // need to re-calculate the new percentage that each fixed amount represents.
                AddBillSplitMethod.EXACT -> {
                    state.splitEntries.map { entry ->
                        val newPercentage = if (state.billAmountAsDouble > 0) (entry.amount / state.billAmountAsDouble) * 100 else 0.0
                        entry.copy(percentage = newPercentage)
                    }
                }
            }
            // Update the state with the newly calculated entries
            _uiState.update { it.copy(splitEntries = newEntries) }
        }
    }

    // validate steps
    fun validateStep(step: Int) {
        val isValid = when (step) {
            1 -> isStepOneValid()
            2 -> isStepTwoValid()
            3 -> isStepThreeValid()
            4 -> isStepFourValid()
            5 -> isStepFiveValid()
            6 -> isStepSixValid()
            7 -> true
            else -> false
        }
        _uiState.update { it.copy(isCurrentStepValid = isValid) }
    }
    private fun isStepOneValid(): Boolean {
        val state = _uiState.value
        return state.billAmountAsDouble > 0.0 &&
                state.description.isNotBlank() &&
                state.category !== null &&
                state.date !== null
    }
    private fun isStepTwoValid(): Boolean{
        val state = _uiState.value
        return (state.isGroupSplit && state.selectedGroupId !== null) ||
                (!state.isGroupSplit && state.selectedFriends.isNotEmpty())
    }
    private fun isStepThreeValid(): Boolean {
        val state = _uiState.value
        return state.participants.size > 1
    }
    private fun isStepFourValid(): Boolean {
        val state = _uiState.value
        return state.paidByUserId !== null
    }
    private fun isStepFiveValid(): Boolean {
        return true
    }
    private fun isStepSixValid(): Boolean {
        val state = _uiState.value
        val tolerance = 0.01 // A small tolerance for comparing floating-point numbers
        return when (state.splitMethod) {
            // For PERCENTAGE, the percentages must sum to 100 AND the resulting amounts must sum to the total bill amount.
            AddBillSplitMethod.PERCENTAGE -> {
                val isPercentageSumValid = (state.sumOfSplitPercentage - 100.0).absoluteValue < tolerance
                val isAmountSumValid = (state.sumOfSplitAmount - state.billAmountAsDouble).absoluteValue < tolerance
                isPercentageSumValid && isAmountSumValid
            }
            // For EXACT, the amounts must sum to the total bill amount AND the resulting percentages must sum to 100.
            AddBillSplitMethod.EXACT -> {
                val isAmountSumValid = (state.sumOfSplitAmount - state.billAmountAsDouble).absoluteValue < tolerance
                val isPercentageSumValid = (state.sumOfSplitPercentage - 100.0).absoluteValue < tolerance
                isAmountSumValid && isPercentageSumValid
            }
            // FOR EQUAL, we skip step six.
            AddBillSplitMethod.EQUAL -> true
        }
    }
    
    fun addBill() {
        viewModelScope.launch { 
            _uiState.update { it.copy(submissionState = AddBillSubmissionState.Loading) }
            val state = _uiState.value
            val data = CreateBillRequest(
                description = state.description,
                category = state.category!!,
                date = state.date!!,
                payerId = state.paidByUserId!!,
                totalAmount = state.billAmount.toInt(),
                splitMethod = state.splitMethod.toString(),
                splits = state.splitEntries.map { entry ->
                    BillSplit(
                        userId = entry.user.userId,
                        amount = (entry.amount * 100).roundToInt(),
                        percentage = entry.percentage
                    )
                }
            )
//            Log.d("Add Bill payload", data.toString())
            val result = repo.addBill(data)
            result.onSuccess { response ->
                _uiState.update { it.copy(submissionState = AddBillSubmissionState.Success(response.message)) }

            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        submissionState = AddBillSubmissionState.Error(
                            exception.message ?: "An error occurred"
                        )
                    )
                }
            }
        }
    }
}