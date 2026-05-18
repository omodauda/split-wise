package com.omodauda.splitwise.ui.features.main.billList

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.ExperimentalMaterial3AdaptiveApi
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.layout.ThreePaneScaffoldDestinationItem
import androidx.compose.material3.adaptive.navigation.NavigableListDetailPaneScaffold
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.omodauda.splitwise.R
import com.omodauda.splitwise.ui.features.main.billDetails.BillDetailsContent
import com.omodauda.splitwise.ui.features.main.billDetails.BillDetailsHeader
import com.omodauda.splitwise.ui.features.main.billDetails.BillDetailsUiState
import com.omodauda.splitwise.ui.features.main.billDetails.BillDetailsViewModel
import com.omodauda.splitwise.ui.features.main.billDetails.ErrorView
import com.omodauda.splitwise.ui.features.main.billDetails.LoadingView
import com.omodauda.splitwise.ui.features.main.home.BillsViewModel
import com.omodauda.splitwise.ui.theme.Spacing
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3AdaptiveApi::class)
@Composable
fun OwedBillListDetailScreen(
    billsViewModel: BillsViewModel,
    detailsViewModel: BillDetailsViewModel,
    goBack: () -> Unit,
    currentUserId: String,
    initialBillId: String? = null
) {
    val navigator = rememberListDetailPaneScaffoldNavigator(
        initialDestinationHistory = if (initialBillId != null) {
            listOf(
                ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List),
                ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.Detail, initialBillId)
            )
        } else {
            listOf(ThreePaneScaffoldDestinationItem(ListDetailPaneScaffoldRole.List))
        }
    )
    val scope = rememberCoroutineScope()
    val bills = billsViewModel.paginatedOwedBills.collectAsLazyPagingItems()
    val uiState by billsViewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by billsViewModel.owedBillSearchQuery.collectAsStateWithLifecycle()
    val selectedSort by billsViewModel.owedSort.collectAsStateWithLifecycle()

    val billDetailsUiState by detailsViewModel.uiState.collectAsStateWithLifecycle()
    val selectedBillId by detailsViewModel.billId.collectAsStateWithLifecycle()

    LaunchedEffect(bills.itemCount, searchQuery) {
        // 1. Handle auto-selection for large screens
        if (initialBillId == null &&
            bills.itemCount > 0 &&
            navigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail] == PaneAdaptedValue.Expanded &&
            navigator.currentDestination?.contentKey == null
        ) {
            val firstBillId = bills[0]?.bill?.id
            if (firstBillId != null) {
                detailsViewModel.setBillId(firstBillId)
                scope.launch {
                    navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, firstBillId)
                }
            }
        }

        // 2. Handle search-driven clearing
        if (searchQuery?.isNotEmpty() == true &&
            selectedBillId != null &&
            bills.loadState.refresh is LoadState.NotLoading
        ) {
            val items = (0 until bills.itemCount).mapNotNull { bills.peek(it) }
            val isPresent = items.any { it.bill.id == selectedBillId }

            if (!isPresent) {
                detailsViewModel.clearSelection()
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
                    BillListHeader(
                        title = stringResource(R.string.bill_title_owed),
                        totalAmount = uiState.billDashboard?.totalOwed ?: 0,
                        totalColor = MaterialTheme.colorScheme.primary,
                        goBack = goBack,
                        paddingTop = Spacing.large,
                        searchQuery = searchQuery,
                        onSearchChanged = { billsViewModel.onOwedBillSearchQueryChanged(it) },
                        selectedSort = selectedSort,
                        onSortChanged = { billsViewModel.onOwedSortChanged(it) }
                    )
                    OwedBillList(
                        bills = bills,
                        selectedBillId = selectedBillId,
                        onRefresh = { bills.refresh() },
                        searchQuery = searchQuery,
                        onClick = { billId ->
                            detailsViewModel.setBillId(billId)
                            // Navigate to detail pane internally
                            scope.launch {
                                navigator.navigateTo(ListDetailPaneScaffoldRole.Detail, billId)
                            }
                        }
                    )
                }
            }
        },
        detailPane =  {
            AnimatedPane {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)) {
                    BillDetailsHeader(
                        goBack = { scope.launch { navigator.navigateBack() } },
                        paddingTop = Spacing.medium,
                        showBackButton = navigator.canNavigateBack()
                    )

                    when (val state = billDetailsUiState) {
                        is BillDetailsUiState.Idle -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Select a bill from the list to view details",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        is BillDetailsUiState.Loading -> LoadingView()
                        is BillDetailsUiState.Error -> ErrorView(state.message, onRetry = { detailsViewModel.fetchBillDetails() })
                        is BillDetailsUiState.Success -> {
                            BillDetailsContent(
                                currentUserId = currentUserId,
                                bill = state.bill,
                                paddingBottom = 0.dp
                            )
                        }
                    }
                }
            }
        }
    )
}