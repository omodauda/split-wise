package com.omodauda.splitwise.ui.features.main.billDetails

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omodauda.splitwise.data.network.model.BillDetails
import com.omodauda.splitwise.data.network.model.GetBillDetailsRequest
import com.omodauda.splitwise.data.repository.BillsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed interface BillDetailsUiState {
    object Idle: BillDetailsUiState
    object Loading: BillDetailsUiState
    data class Success(val bill: BillDetails): BillDetailsUiState
    data class Error(val message: String): BillDetailsUiState
}

@HiltViewModel
class BillDetailsViewModel @Inject constructor(private val repo: BillsRepository, savedStateHandle: SavedStateHandle): ViewModel() {
    private val initialBillId: String? = savedStateHandle["billId"]
    private val _billId = MutableStateFlow(initialBillId)
    val billId = _billId.asStateFlow()

    private val _uiState = MutableStateFlow(
        if (initialBillId != null) BillDetailsUiState.Loading else BillDetailsUiState.Idle
    )
    val uiState = _uiState.asStateFlow()

    init {
        initialBillId?.let { fetchBillDetails(it) }
    }

    fun setBillId(id: String) {
        if (_billId.value == id) return
        _billId.value = id
        fetchBillDetails(id)
    }

    fun clearSelection() {
        _billId.value = null
        _uiState.update { BillDetailsUiState.Idle }
    }

    fun fetchBillDetails(billId: String? = _billId.value) {
        val targetId = billId ?: return
        viewModelScope.launch {
            _uiState.update { BillDetailsUiState.Loading }
            repo.getBillDetails(data = GetBillDetailsRequest(targetId))
                .onSuccess { data ->
                    _uiState.update { BillDetailsUiState.Success(data) }
                }
                .onFailure { exception ->
                    _uiState.update { BillDetailsUiState.Error(exception.message ?: "An unknown error occurred") }
                }
        }
    }
}