package com.example.splitwise.ui.features.main.addBill

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.splitwise.model.AddBillUiState
import com.example.splitwise.model.SplitEntryState
import com.example.splitwise.model.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AddBillViewModel: ViewModel() {
    private val _uiState = MutableStateFlow(AddBillUiState())
    val uiState: StateFlow<AddBillUiState> = _uiState.asStateFlow()

    fun onBillAmountChange(amount: Double) {
        _uiState.update { it.copy(billAmount = amount) }
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

    fun onGroupMemberSelected(member: User) {
        _uiState.update { currentState ->
            val updatedParticipants = currentState.participants.toMutableList()
            val isAlreadySelected = updatedParticipants.any { it.id == member.id }

            if (isAlreadySelected) {
                updatedParticipants.removeAll { it.id == member.id }
            } else {
                updatedParticipants.add(member)
            }

            // Also update the split entries to match the new participant list
            val newEntries = updatedParticipants.map { user ->
                currentState.splitEntries.find { it.user.id == user.id } ?: SplitEntryState(user)
            }

            currentState.copy(
                participants = updatedParticipants,
                splitEntries = newEntries
            )
        }
        recalculateSplits()
         validateStep(3)
    }

    fun onFriendSelected(friend: User) {
        _uiState.update { currentState ->
            val updatedFriends = currentState.selectedFriends.toMutableList()
            val updatedParticipants = currentState.participants.toMutableList()

            val isAlreadySelected = updatedFriends.contains(friend.id)
            if (isAlreadySelected) {
                updatedFriends.remove(friend.id)
                updatedParticipants.removeAll { it.id == friend.id }
            } else {
                updatedFriends.add(friend.id)
                updatedParticipants.add(friend)
            }

            val newEntries = updatedParticipants.map { user ->
                currentState.splitEntries.find { it.user.id == user.id } ?: SplitEntryState(user)
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
        // TODO: set friends as participants
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
        val amount = (_uiState.value.billAmount * percentage) / 100.0
        updateSplitEntry(userId, amount, percentage)
    }

    fun onExactAmountChanged(userId: String, newAmount: String) {
        val amount = newAmount.toDoubleOrNull() ?: 0.00
        val percentage = if (_uiState.value.billAmount > 0) (amount / _uiState.value.billAmount) * 100 else 0.00
        updateSplitEntry(userId, amount, percentage)
    }

    fun splitEqually() {
        viewModelScope.launch {
            val state = _uiState.value
            if (state.participants.isEmpty()) return@launch

            val equalAmount = state.billAmount / state.participants.size
            val equalPercentage = if (state.billAmount > 0) (equalAmount / state.billAmount) * 100 else 0.0

            _uiState.update {
                it.copy(
                    splitEntries = it.participants.map { user ->
                        SplitEntryState(user, equalAmount, equalPercentage)
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
                val evenAmount = state.billAmount / state.participants.size
                updateAllSplitEntries(evenAmount, evenPercentage)
            }
            AddBillSplitMethod.EXACT -> {
                val state = _uiState.value
                if (state.participants.isEmpty()) return
                val evenAmount = state.billAmount / state.participants.size
                val evenPercentage = if (state.billAmount > 0) (evenAmount / state.billAmount) * 100 else 0.0
                updateAllSplitEntries(evenAmount, evenPercentage)
            }
            AddBillSplitMethod.EQUAL -> {
                // Already handled by splitEqually()
            }
        }
    }


    private fun initializeSplitEntries(participants: List<User>) {
        val entries = participants.map { user -> SplitEntryState(user = user) }
        _uiState.update { it.copy(splitEntries = entries) }
        // If the default method is EQUAL, immediately perform the split.
        if (_uiState.value.splitMethod == AddBillSplitMethod.EQUAL) {
//            splitEqually()
        }
    }

    private fun updateSplitEntry(userId: String, newAmount: Double, newPercentage: Double) {
        _uiState.update { currentState ->
            currentState.copy(
                splitEntries = currentState.splitEntries.map { entry ->
                    if (entry.user.id == userId) {
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
                    val equalAmount = state.billAmount / state.participants.size
                    state.splitEntries.map { it.copy(amount = equalAmount) }
                }

                // If the method is PERCENTAGE, keep the percentages and re-calculate the amounts.
                AddBillSplitMethod.PERCENTAGE -> {
                    state.splitEntries.map { entry ->
                        val newAmount = (state.billAmount * entry.percentage) / 100.0
                        entry.copy(amount = newAmount)
                    }
                }

                // If the method is EXACT, the amounts are fixed by the user, so we just
                // need to re-calculate the new percentage that each fixed amount represents.
                AddBillSplitMethod.EXACT -> {
                    state.splitEntries.map { entry ->
                        val newPercentage = if (state.billAmount > 0) (entry.amount / state.billAmount) * 100 else 0.0
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
            else -> false
        }
        _uiState.update { it.copy(isCurrentStepValid = isValid) }
    }

    private fun isStepOneValid(): Boolean {
        val state = _uiState.value
        return state.billAmount > 0.0 &&
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
        val state = _uiState.value
        return true
    }
}