package com.example.splitwise.ui.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.splitwise.data.network.model.OwedBill
import com.example.splitwise.data.network.model.OwingBill
import com.example.splitwise.data.repository.BillsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class BillsUiState(
    val owedBills: List<OwedBill> = emptyList(),
    val isOwedBillsLoading: Boolean = false,

    val owingBills: List<OwingBill> = emptyList(),
    val isOwingBillsLoading: Boolean = false
)

class BillsViewModel(private val repo: BillsRepository): ViewModel() {

    private val refreshTrigger = MutableSharedFlow<Unit>(replay = 1)
    // Paginated bills
    @OptIn(ExperimentalCoroutinesApi::class)
    val paginatedOwedBills = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest { repo.getOwedBillsStream() }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val paginatedOwingBills = refreshTrigger
        .onStart { emit(Unit) }
        .flatMapLatest { repo.getOwingBillsStream() }
        .cachedIn(viewModelScope)

    // owed bills first page
    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPreview()
    }

    fun refresh() {
        loadPreview()
        // Refresh the Paginated streams
        viewModelScope.launch {
            refreshTrigger.emit(Unit)
        }
    }
    private fun loadPreview() {
        viewModelScope.launch {
            _uiState.update { it.copy(isOwedBillsLoading = true, isOwingBillsLoading = true) }
            launch {
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
            launch {
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

}