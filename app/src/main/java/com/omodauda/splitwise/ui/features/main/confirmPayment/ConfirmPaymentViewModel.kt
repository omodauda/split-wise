package com.omodauda.splitwise.ui.features.main.confirmPayment

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omodauda.splitwise.data.network.model.PendingPayment
import com.omodauda.splitwise.data.repository.BillsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

sealed interface ConfirmPaymentUiState {
    object Loading : ConfirmPaymentUiState
    data class Success(val payment: PendingPayment) : ConfirmPaymentUiState
    data class Error(val message: String) : ConfirmPaymentUiState
}

sealed interface ConfirmActionState {
    object Idle : ConfirmActionState
    object Loading : ConfirmActionState
    data class Success(val message: String) : ConfirmActionState
    data class Error(val message: String) : ConfirmActionState
}

@HiltViewModel
class ConfirmPaymentViewModel @Inject constructor(
    private val repository: BillsRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private var paymentId: String? = savedStateHandle["paymentId"]

    private val _uiState = MutableStateFlow<ConfirmPaymentUiState>(ConfirmPaymentUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private val _confirmActionState = MutableStateFlow<ConfirmActionState>(ConfirmActionState.Idle)
    val confirmActionState = _confirmActionState.asStateFlow()

    init {
        paymentId?.let { fetchPaymentDetails(it) }
    }

    fun setPaymentId(id: String) {
        if (paymentId != id) {
            paymentId = id
            fetchPaymentDetails(id)
        }
    }

    fun fetchPaymentDetails(id: String? = paymentId) {
        val targetId = id ?: return
        viewModelScope.launch {
            _uiState.update { ConfirmPaymentUiState.Loading }
             repository.getPaymentDetails(targetId)
                .onSuccess { data -> _uiState.update { ConfirmPaymentUiState.Success(data.data) } }
                .onFailure { e -> _uiState.update { ConfirmPaymentUiState.Error(e.message ?: "Error") } }
        }
    }

    fun confirmPayment() {
        val targetId = paymentId ?: return
        viewModelScope.launch {
            _confirmActionState.update { ConfirmActionState.Loading }
             val result = repository.confirmPayment(targetId)
             result.onSuccess { msg ->
                 repository.triggerRefreshPaymentPendingConfirmation()
                 _confirmActionState.update { ConfirmActionState.Success(msg.message) }
             }.onFailure { e ->
                 _confirmActionState.update { ConfirmActionState.Error(e.message ?: "Error") }
             }
        }
    }

    fun resetActionState() {
        _confirmActionState.update { ConfirmActionState.Idle }
    }
}