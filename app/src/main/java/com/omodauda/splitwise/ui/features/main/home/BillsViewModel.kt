package com.omodauda.splitwise.ui.features.main.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.omodauda.splitwise.data.network.model.GetBillsDashboardResponse
import com.omodauda.splitwise.data.network.model.OwedBill
import com.omodauda.splitwise.data.network.model.OwingBill
import com.omodauda.splitwise.data.network.model.PayBillRequest
import com.omodauda.splitwise.data.network.model.SendBillReminderRequest
import com.omodauda.splitwise.data.repository.BillsRepository
import com.omodauda.splitwise.ui.features.main.billList.BillSortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

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

@HiltViewModel
class BillsViewModel @Inject constructor(private val repo: BillsRepository) : ViewModel() {
    private val _owedSort = MutableStateFlow<BillSortOption?>(BillSortOption.MOST_RECENT)
    val owedSort = _owedSort.asStateFlow()

    private val _owedBillSearchQuery = MutableStateFlow<String?>(null)
    val owedBillSearchQuery = _owedBillSearchQuery.asStateFlow()

    private val _owingSort = MutableStateFlow<BillSortOption?>(BillSortOption.MOST_RECENT)
    val owingSort = _owingSort.asStateFlow()

    private val _owingBillSearchQuery = MutableStateFlow<String?>(null)
    val owingBillSearchQuery = _owingBillSearchQuery.asStateFlow()

    // Paginated bills
    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val paginatedOwedBills = combine(
        repo.refreshOwedBillSignal.onStart { emit(Unit) },
        owedBillSearchQuery.debounce { query ->
            if (query.isNullOrBlank()) 0L else 500L
        },
        _owedSort
    ) { _, query, sort ->
        val sortValue = when (sort) {
//            BillSortOption.AMOUNT_HIGH_TO_LOW -> "amount_high"
//            BillSortOption.AMOUNT_LOW_TO_HIGH -> "amount_low"
            BillSortOption.MOST_RECENT -> "recent"
            BillSortOption.ASC -> "name_asc"
            BillSortOption.DESC -> "name_desc"
            else -> null
        }
        query to sortValue
    }.flatMapLatest { (query, sortBy) -> repo.getOwedBillsStream(query, sortBy) }
        .cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class, FlowPreview::class)
    val paginatedOwingBills = combine(
        repo.refreshOwingBillSignal.onStart { emit(Unit) },
        owingBillSearchQuery.debounce { query ->
            if (query.isNullOrBlank()) 0L else 500L
        },
        _owingSort
    ) { _, query, sort ->
        val sortValue = when (sort) {
//            BillSortOption.AMOUNT_HIGH_TO_LOW -> "amount_high"
//            BillSortOption.AMOUNT_LOW_TO_HIGH -> "amount_low"
            BillSortOption.MOST_RECENT -> "recent"
            BillSortOption.ASC -> "name_asc"
            BillSortOption.DESC -> "name_desc"
            else -> null
        }
        query to sortValue
    }.flatMapLatest { (query, sortBy) -> repo.getOwingBillsStream(query, sortBy) }
        .cachedIn(viewModelScope)



    private val _uiState = MutableStateFlow(BillsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        loadPreview()
        observeRefreshSignals()
    }

    @OptIn(FlowPreview::class)
    private fun observeRefreshSignals() {
        viewModelScope.launch {
            // Dashboard: Consolidate signals to avoid redundant fetching
            launch {
                merge(repo.refreshOwedBillSignal, repo.refreshOwingBillSignal)
                    .debounce(100L)
                    .collect {
                        repo.getBillsDashboard().onSuccess { data ->
                            _uiState.update {
                                it.copy(
                                    billDashboard = data,
                                    dashboardLoading = false
                                )
                            }
                        }.onFailure {
                            _uiState.update { it.copy(dashboardLoading = false) }
                        }
                    }
            }

            // Observe Owed Signals
            launch {
                repo.refreshOwedBillSignal.collect {
                    silentRefresh(refreshOwed = true)
                }
            }
            // Observe Owing Signals
            launch {
                repo.refreshOwingBillSignal.collect {
                    silentRefresh(refreshOwing = true)
                }
            }
        }
    }

    /**
     * Performs a background refresh without showing loading spinners.
     */
    private suspend fun silentRefresh(
        refreshOwed: Boolean = false,
        refreshOwing: Boolean = false
    ) = coroutineScope {

        if (refreshOwed) {
            launch {
                repo.getOwedBillsFirstPage().onSuccess { bills ->
                    _uiState.update { it.copy(owedBills = bills) }
                }
            }
        }

        if (refreshOwing) {
            launch {
                repo.getOwingBillsFirstPage().onSuccess { bills ->
                    _uiState.update { it.copy(owingBills = bills) }
                }
            }
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

    fun sendBillReminder(data: SendBillReminderRequest) {
        viewModelScope.launch {
            repo.sendBillReminder(data)
        }
    }

    fun resetSubmissionState() {
        _uiState.update { it.copy(billActionState = PayBillSubmissionState.Idle) }
    }

    fun onOwedBillSearchQueryChanged(query: String?) {
        _owedBillSearchQuery.update { query }
    }

    fun onOwedSortChanged(sort: BillSortOption) {
        _owedSort.update { sort }
    }

    fun onOwingBillSearchQueryChanged(query: String?) {
        _owingBillSearchQuery.update { query }
    }

    fun onOwingSortChanged(sort: BillSortOption) {
        _owingSort.update { sort }
    }
}