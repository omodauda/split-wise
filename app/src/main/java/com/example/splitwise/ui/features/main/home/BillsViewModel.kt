package com.example.splitwise.ui.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.splitwise.data.network.model.OwedBill
import com.example.splitwise.data.network.model.OwingBill
import com.example.splitwise.data.repository.BillsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BillsUiState(
    val owedBills: List<OwedBill> = emptyList(),
    val isOwedBillsLoading: Boolean = false,

    val owingBills: List<OwingBill> = emptyList(),
    val isOwingBillsLoading: Boolean = false
)

class BillsViewModel(private val repo: BillsRepository): ViewModel() {
    // Paginated bills
    val paginatedOwedBills = repo.getOwedBillsStream().cachedIn(viewModelScope)
    val paginatedOwingBills = repo.getOwingBillsStream().cachedIn(viewModelScope)

    // owed bills first page
    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPreview()
    }
    private fun loadPreview() {
        viewModelScope.launch {
            _uiState.update { it.copy(isOwedBillsLoading = true, isOwingBillsLoading = true) }
            repo.getOwedBillsFirstPage()
                .onSuccess { bills ->
                    _uiState.update {
                        it.copy(
                            owedBills = bills,
                            isOwedBillsLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isOwedBillsLoading = false
                        )
                    }
                }
            repo.getOwingBillsFirstPage()
                .onSuccess { bills ->
                    _uiState.update {
                        it.copy(
                            owingBills = bills,
                            isOwingBillsLoading = false
                        )
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            isOwingBillsLoading = false
                        )
                    }
                }
        }
    }

}