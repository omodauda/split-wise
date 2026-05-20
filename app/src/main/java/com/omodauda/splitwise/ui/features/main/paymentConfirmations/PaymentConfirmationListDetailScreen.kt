package com.omodauda.splitwise.ui.features.main.paymentConfirmations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.layout.calculatePaneScaffoldDirective
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.omodauda.splitwise.ui.components.LoadingView
import com.omodauda.splitwise.ui.features.main.billDetails.ErrorView
import com.omodauda.splitwise.ui.features.main.confirmPayment.BillDetailsView
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmActionState
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentFooter
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentHeader
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentUiState
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentViewModel
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmationNote
import com.omodauda.splitwise.ui.features.main.confirmPayment.PaymentInfo
import com.omodauda.splitwise.ui.features.main.paymentConfirmations.components.PaymentConfirmationHeader
import com.omodauda.splitwise.ui.features.main.paymentConfirmations.components.PaymentConfirmationList
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.utils.formatFromCents
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun PaymentConfirmationListDetailScreen(
    viewModel: PaymentPendingConfirmationViewModel,
    confirmPaymentViewModel: ConfirmPaymentViewModel,
    goBack: () -> Unit,
    initialPaymentId: String? = null
) {
    val scaffoldDirective = calculatePaneScaffoldDirective(currentWindowAdaptiveInfo())
    val customDirective = scaffoldDirective.copy(
        horizontalPartitionSpacerSize = 0.dp
    )
    val navigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = customDirective,
        initialDestinationHistory = if (initialPaymentId != null) {
            listOf(
                ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List),
                ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.Detail, initialPaymentId)
            )
        } else {
            listOf(ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List))
        }
    )
    val scope = rememberCoroutineScope()
    val payments = viewModel.pendingPaymentPagingData.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()
    val selectedSort by viewModel.sort.collectAsStateWithLifecycle()
    val totalCount by viewModel.totalCount.collectAsStateWithLifecycle()

    val confirmPaymentUiState by confirmPaymentViewModel.uiState.collectAsStateWithLifecycle()
    val confirmActionState by confirmPaymentViewModel.confirmActionState.collectAsStateWithLifecycle()
    val selectedPaymentId by confirmPaymentViewModel.paymentId.collectAsStateWithLifecycle()

    LaunchedEffect(confirmActionState) {
        if (confirmActionState is ConfirmActionState.Success) {
            confirmPaymentViewModel.resetActionState()
            if (navigator.canNavigateBack()) {
                scope.launch {
                    navigator.navigateBack()
                }
            } else {
                confirmPaymentViewModel.clearSelection()
            }
        }
    }

    LaunchedEffect(payments.itemCount, searchQuery) {
        // 1. Handle auto-selection for large screens
        if (initialPaymentId == null &&
            payments.itemCount > 0 &&
            navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded &&
            navigator.currentDestination?.contentKey == null
        ) {
            val firstPaymentId = payments[0]?.id
            if (firstPaymentId != null) {
                confirmPaymentViewModel.setPaymentId(firstPaymentId)
                scope.launch {
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, firstPaymentId)
                }
            }
        }

        // 2. Handle search-driven clearing
        if (searchQuery?.isNotEmpty() == true &&
            selectedPaymentId != null &&
            payments.loadState.refresh is LoadState.NotLoading
        ) {
            val items = (0 until payments.itemCount).mapNotNull { payments.peek(it) }
            val isPresent = items.any { it.id == selectedPaymentId }

            if (!isPresent) {
                confirmPaymentViewModel.clearSelection()
            }
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = navigator,
        listPane = {
            AnimatedPane {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)) {
                    PaymentConfirmationHeader(
                        goBack = goBack,
                        paddingTop = Spacing.large,
                        searchQuery = searchQuery,
                        onSearchChanged = { viewModel.onSearchQueryChanged(it) },
                        selectedSort = selectedSort,
                        onSortChanged = { viewModel.onSortChanged(it) },
                        totalCount = totalCount
                    )
                    PaymentConfirmationList(
                        payments = payments,
                        onRefresh = { payments.refresh() },
                        searchQuery = searchQuery,
                        selectedPaymentId = selectedPaymentId,
                        onClick = { paymentId ->
                            confirmPaymentViewModel.setPaymentId(paymentId)
                            scope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, paymentId)
                            }
                        }
                    )
                }
            }
        },
        detailPane = {
            AnimatedPane {
                Scaffold(
                    bottomBar = {
                        if (confirmPaymentUiState is ConfirmPaymentUiState.Success) {
                            ConfirmPaymentFooter(
                                onConfirm = { confirmPaymentViewModel.confirmPayment() }
                            )
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(MaterialTheme.colorScheme.background)
                            .padding(
                                bottom = innerPadding.calculateBottomPadding(),
                                start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                            )
                    ) {
                        ConfirmPaymentHeader(
                            goBack = { scope.launch { navigator.navigateBack() } },
                            showBackButton = navigator.canNavigateBack()
                        )

                        Box(modifier = Modifier.fillMaxSize()) {
                            when (val state = confirmPaymentUiState) {
                                is ConfirmPaymentUiState.Idle -> {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "Select a payment to view details",
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                is ConfirmPaymentUiState.Loading -> LoadingView()
                                is ConfirmPaymentUiState.Error -> ErrorView(
                                    message = state.message,
                                    onRetry = { confirmPaymentViewModel.fetchPaymentDetails() })

                                is ConfirmPaymentUiState.Success -> {
                                    val payment = state.payment
                                    Column(
                                        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(horizontal = Spacing.medium)
                                            .verticalScroll(rememberScrollState())
                                    ) {
                                        Spacer(Modifier.height(Spacing.medium))
                                        BillDetailsView(
                                            category = payment.bill.category,
                                            description = payment.bill.description,
                                            totalBillAmount = payment.bill.totalAmount,
                                            splitAmount = payment.split.amount
                                        )
                                        PaymentInfo(
                                            fullName = payment.payer.fullName,
                                            amountPaid = payment.amount,
                                            datePaid = payment.createdAt
                                        )
                                        ConfirmationNote(
                                            fullName = payment.payer.fullName,
                                            paidAmount = formatFromCents(payment.amount)
                                        )
                                        Spacer(Modifier.height(Spacing.medium))
                                    }
                                }
                            }

                            if (confirmActionState is ConfirmActionState.Loading) {
                                LoadingView()
                            }
                        }
                    }
                }
            }
        }
    )
}
