package com.example.splitwise.ui.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.splitwise.data.network.model.OwedBill
import com.example.splitwise.data.repository.BillsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BillsUiState(
    val owedBills: List<OwedBill> = emptyList(),
    val isOwedBillsLoading: Boolean = false
)

class BillsViewModel(private val repo: BillsRepository): ViewModel() {
    // Paginated owed bills
    val paginatedOwedBills = repo.getOwedBillsStream().cachedIn(viewModelScope)

    // owed bills first page
    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPreview()
    }
    private fun loadPreview() {
        viewModelScope.launch {
            _uiState.update { it.copy(isOwedBillsLoading = true) }
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
        }
    }

}