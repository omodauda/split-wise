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
    object Loading: BillDetailsUiState
    data class Success(val bill: BillDetails): BillDetailsUiState
    data class Error(val message: String): BillDetailsUiState
}

@HiltViewModel
class BillDetailsViewModel @Inject constructor(private val repo: BillsRepository, savedStateHandle: SavedStateHandle): ViewModel() {
    private val billId: String = checkNotNull(savedStateHandle["billId"])

    private val _uiState = MutableStateFlow<BillDetailsUiState>(BillDetailsUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchBillDetails()
    }

    fun fetchBillDetails() {
        viewModelScope.launch {
            _uiState.update { BillDetailsUiState.Loading }
            repo.getBillDetails(data = GetBillDetailsRequest(billId))
                .onSuccess { data ->
                    _uiState.update { BillDetailsUiState.Success(data) }
                }
                .onFailure { exception ->
                    _uiState.update { BillDetailsUiState.Error(exception.message ?: "An unknown error occurred") }
                }
        }
    }
}