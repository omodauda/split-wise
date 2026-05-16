package com.omodauda.splitwise.ui.features.main.billList

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.OwingBill
import com.omodauda.splitwise.ui.features.main.friends.FriendListPlaceholder
import com.omodauda.splitwise.ui.features.main.friends.FriendPlaceholder
import com.omodauda.splitwise.ui.features.main.home.BillsViewModel
import com.omodauda.splitwise.ui.theme.ComponentDimensions
import com.omodauda.splitwise.ui.theme.CurrencySmall
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.utils.formatDate
import com.omodauda.splitwise.utils.formatFromCents
import java.util.Date

@Composable
fun OwingBillListScreen(
    viewModel: BillsViewModel,
    goBack: () -> Unit,
    goToBillDetails: (billId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bills = viewModel.paginatedOwingBills.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.owingBillSearchQuery.collectAsStateWithLifecycle()
    val selectedSort by viewModel.owingSort.collectAsStateWithLifecycle()

    val title = stringResource(R.string.bill_title_owing)
    val totalAmount = uiState.billDashboard?.totalOwing ?: 0

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = innerPadding.calculateStartPadding(LayoutDirection.Ltr), end = innerPadding.calculateEndPadding(
                    LayoutDirection.Ltr))
        ) {
            BillListHeader(
                title = title,
                totalAmount = totalAmount,
                totalColor = MaterialTheme.colorScheme.error,
                goBack = goBack,
                paddingTop = innerPadding.calculateTopPadding(),
                searchQuery = searchQuery,
                onSearchChanged = {viewModel.onOwingBillSearchQueryChanged(it)},
                selectedSort = selectedSort,
                onSortChanged = { viewModel.onOwingSortChanged(it) }
            )
            OwingBillList(
                bills,
                onRefresh = {bills.refresh()},
                searchQuery = searchQuery,
                onClick = { goToBillDetails(it) }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OwingBillList(
    bills: LazyPagingItems<OwingBill>,
    onRefresh: () -> Unit,
    searchQuery: String?,
    onClick: (billId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val loadState = bills.loadState
    val isRefreshing = bills.loadState.refresh is LoadState.Loading

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (loadState.refresh is LoadState.Loading) {
            // handle refresh loading
            FriendListPlaceholder()
        } else if (!searchQuery.isNullOrBlank() && bills.itemCount == 0) {
            EmptyBillSearchView()
        } else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh()},
                modifier = modifier.fillMaxSize()
            ) {
                LazyColumn(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    contentPadding = PaddingValues(
                        bottom = Spacing.large,
                        top = Spacing.medium
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    items(
                        count = bills.itemCount,
                        key = bills.itemKey { it.id }) { index ->
                        val bill = bills[index]
                        if (bill !== null) {
                            val remainder = bill.amount - bill.paidAmount
                            OwingBillListItem(
                                personName = bill.bill.paidBy.fullName,
                                remainingAmount = formatFromCents(remainder),
                                descriptionText = bill.bill.description,
                                date = bill.createdAt,
                                modifier = Modifier
                                    .widthIn(max = 500.dp)
                                    .clickable(enabled = true, onClick = {onClick(bill.bill.id)}),

                                )
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        } else {
                            // TODO: show skeleton placeholder
                            FriendPlaceholder()
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OwingBillListItem(
    personName: String,
    descriptionText: String,
    remainingAmount: String,
    date: Date,
    modifier: Modifier = Modifier,
) {
    val avatarText = personName[0].uppercase()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .padding(ScreenDimensions.contentPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(ComponentDimensions.avatarSizeMedium)
                    .clip(shape = CircleShape)
                    .background(color = MaterialTheme.colorScheme.errorContainer)
            ) {
                Text(
                    text = avatarText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.error
                )
            }
            Column{
                Text(
                    text = personName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = remainingAmount,
                    style = CurrencySmall,
                    color = MaterialTheme.colorScheme.error
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = formatDate(date),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            Icon(
                painter = painterResource(R.drawable.caret_right),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(12.dp)
            )
        }
    }
}