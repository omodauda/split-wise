package com.example.splitwise.ui.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.example.splitwise.data.network.model.GetBillsDashboardResponse
import com.example.splitwise.data.network.model.OwedBill
import com.example.splitwise.data.network.model.OwingBill
import com.example.splitwise.data.network.model.PayBillRequest
import com.example.splitwise.data.repository.BillsRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface PayBillSubmissionState {
    data object Idle : PayBillSubmissionState
    data object Loading : PayBillSubmissionState
    data class Error(val message: String) : PayBillSubmissionState
    data class Success(val message: String) : PayBillSubmissionState
}

data class BillsUiState(
    val owedBills: List<OwedBill> = emptyList(),
    val isOwedBillsLoading: Boolean = false,

    val owingBills: List<OwingBill> = emptyList(),
    val isOwingBillsLoading: Boolean = false,

    val dashboardLoading: Boolean = false,
    val billDashboard: GetBillsDashboardResponse? = null,

    val billActionState: PayBillSubmissionState = PayBillSubmissionState.Idle,
)

class BillsViewModel(private val repo: BillsRepository) : ViewModel() {

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
            _uiState.update {
                it.copy(
                    isOwedBillsLoading = true,
                    isOwingBillsLoading = true,
                    dashboardLoading = true
                )
            }
            launch {
                repo.getBillsDashboard()
                    .onSuccess { data ->
                        _uiState.update {
                            it.copy(
                                billDashboard = data,
                                dashboardLoading = false
                            )
                        }
                    }
                    .onFailure {
                        _uiState.update {
                            it.copy(
                                dashboardLoading = false
                            )
                        }
                    }
            }
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

    fun payBill(data: PayBillRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(billActionState = PayBillSubmissionState.Loading) }
            val result = repo.payBill(data)
            result.onSuccess { message ->
                refresh()
                _uiState.update { it.copy(billActionState = PayBillSubmissionState.Success(message)) }
            }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            billActionState = PayBillSubmissionState.Error(
                                exception.message ?: "An error occurred"
                            )
                        )
                    }
                }
        }
    }

    fun settleBill(data: PayBillRequest) {
        viewModelScope.launch {
            _uiState.update { it.copy(billActionState = PayBillSubmissionState.Loading) }
            val result = repo.settleBill(data)
            result.onSuccess { message ->
                refresh()
                _uiState.update { it.copy(billActionState = PayBillSubmissionState.Success(message)) }
            }
                .onFailure { exception ->
                    _uiState.update {
                        it.copy(
                            billActionState = PayBillSubmissionState.Error(
                                exception.message ?: "An error occurred"
                            )
                        )
                    }
                }
        }
    }

}