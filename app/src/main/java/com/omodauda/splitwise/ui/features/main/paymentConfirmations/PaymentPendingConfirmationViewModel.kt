package com.omodauda.splitwise.ui.features.main.paymentConfirmations

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.omodauda.splitwise.data.network.model.PendingPayment
import com.omodauda.splitwise.data.repository.BillsRepository
import com.omodauda.splitwise.ui.features.main.confirmPayment.PendingPaymentSortOption
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PaymentPendingConfirmationViewModel @Inject constructor(private val repository: BillsRepository): ViewModel() {
    private val _searchQuery = MutableStateFlow<String?>(null)
    val searchQuery = _searchQuery.asStateFlow()

    private val _sort =
        MutableStateFlow<PendingPaymentSortOption?>(PendingPaymentSortOption.MOST_RECENT)
    val sort = _sort.asStateFlow()

    private val _totalCount = MutableStateFlow(0)
    val totalCount = _totalCount.asStateFlow()

    private val _firstPendingPayment = MutableStateFlow<PendingPayment?>(null)
    val firstPendingPayment = _firstPendingPayment.asStateFlow()

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private var hasBeenShown = false


    init {
        viewModelScope.launch {
            repository.paymentPendingConfirmationSignal.onStart { emit(Unit) }.collect {
                fetchTotalPending()
            }
        }
    }

    fun fetchTotalPending() {
        viewModelScope.launch {
            repository.getPaymentPendingConfirmationFirstPage().onSuccess { data ->
                _totalCount.value = data.totalCount
                if (data.data.isNotEmpty()) {
                    _firstPendingPayment.value = data.data.first()

                    if (!hasBeenShown) {
                        _showDialog.value = true
                        hasBeenShown = true
                    }
                }
            }
        }
    }

    fun onDialogDismissed() {
        _showDialog.value = false
    }

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val pendingPaymentPagingData: Flow<PagingData<PendingPayment>> = combine(
        repository.paymentPendingConfirmationSignal.onStart { emit(Unit) },
        _searchQuery.debounce(500L),
        _sort
    ) {_, query, sortOption ->
        val sortValue = when (sortOption) {
            PendingPaymentSortOption.AMOUNT_HIGH_TO_LOW -> "amount_high"
            PendingPaymentSortOption.AMOUNT_LOW_TO_HIGH -> "amount_low"
            PendingPaymentSortOption.MOST_RECENT -> "recent"
            else -> null
        }
        query to sortValue
    }.flatMapLatest { (query, sortValue) ->
        repository.getPaymentPendingConfirmationStream(query, sortValue)
    }.cachedIn(viewModelScope)

    fun onSearchQueryChanged(query: String?) {
        _searchQuery.value = query
    }

    fun onSortChanged(sort: PendingPaymentSortOption) {
        _sort.update { sort }
    }
}