package com.omodauda.splitwise.ui.features.main.paymentConfirmations

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.OwingBill
import com.omodauda.splitwise.ui.components.AppTextField
import com.omodauda.splitwise.ui.features.main.billList.BillSortOption
import com.omodauda.splitwise.ui.features.main.billList.EmptyBillSearchView
import com.omodauda.splitwise.ui.features.main.billList.SortButton
import com.omodauda.splitwise.ui.features.main.billList.SortDropdownMenu
import com.omodauda.splitwise.ui.features.main.friends.FriendListPlaceholder
import com.omodauda.splitwise.ui.features.main.friends.FriendPlaceholder
import com.omodauda.splitwise.ui.features.main.home.BillsViewModel
import com.omodauda.splitwise.ui.features.main.home.components.AvatarView
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.crystalPeak
import com.omodauda.splitwise.utils.formatFromCents
import java.text.SimpleDateFormat
import java.util.Date

@Composable
fun PaymentConfirmationScreen(
    viewModel: BillsViewModel,
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bills = viewModel.paginatedOwingBills.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by rememberSaveable {mutableStateOf("") }
    val selectedSort by rememberSaveable {mutableStateOf(BillSortOption.MOST_RECENT) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            PaymentConfirmationHeader(
                goBack = goBack,
                paddingTop = innerPadding.calculateTopPadding(),
                searchQuery = searchQuery,
                onSearchChanged = {},
                selectedSort = selectedSort,
                onSortChanged = {  }
            )
            PaymentConfirmationList(
                bills,
                onRefresh = {bills.refresh()},
                searchQuery = searchQuery,
                onClick = {  }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentConfirmationList(
    bills: LazyPagingItems<OwingBill>,
    onRefresh: () -> Unit,
    searchQuery: String?,
    onClick: (paymentId: String) -> Unit,
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
                    verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
                    contentPadding = PaddingValues(
                        bottom = Spacing.large,
                        start = Spacing.medium,
                        end = Spacing.medium,
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
                            PaymentConfirmationItem(
                                fullName = "Sarah Johnson",
                                amountPaid = 1680,
                                datePaid = Date(),
                                description = "Electricity",
                                category = "Utilities",
                                modifier = Modifier
                                    .clickable(onClick = {onClick(bill.id)})
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
fun PaymentConfirmationHeader(
    goBack: () -> Unit,
    paddingTop: Dp,
    searchQuery: String?,
    onSearchChanged: (String?) -> Unit,
    onSortChanged: (BillSortOption) -> Unit,
    modifier: Modifier = Modifier,
    selectedSort: BillSortOption? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .shadow(elevation = 1.dp)
            .padding(
                start = Spacing.medium,
                end = Spacing.medium,
                top = paddingTop + Spacing.small,
                bottom = Spacing.medium
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
        ) {
            IconButton(
                onClick = {goBack()}
            ) {
                Icon(
                    painter = painterResource(R.drawable.caret_left),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.payment_confirmation),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "3 ${stringResource(R.string.pending)}",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
        Spacer(Modifier.height(Spacing.medium))
        AppTextField(
            value = searchQuery ?: "",
            onValueChange = {onSearchChanged(it)},
            placeholder = stringResource(R.string.search_bill),
            leadingIcon = R.drawable.search_icon
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            SortButton(
                onClick = {expanded = !expanded},
                sortTitle = selectedSort?.label,
                modifier = Modifier
                    .weight(1f)
            )
        }
        SortDropdownMenu(
            expanded = expanded,
            selectedSort = selectedSort,
            onSortChanged = {onSortChanged(it)},
            onDismiss = {expanded = false}
        )
    }
}

@Composable
fun PaymentConfirmationItem(
    fullName: String,
    amountPaid: Int,
    datePaid: Date,
    description: String,
    category: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(2.dp, shape = RoundedCornerShape(14.dp))
            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(14.dp))
            .padding(Spacing.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarView(avatarText = fullName[0].toString())
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.paid_share),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = formatFromCents(amountPaid),
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.primary
                )
                val formatter = SimpleDateFormat("MMM dd", java.util.Locale.getDefault())
                Text(
                    text = formatter.format(datePaid),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.small),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(color = crystalPeak, shape = RoundedCornerShape(14.dp))
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(vertical = 2.dp, horizontal = 8.dp)
                )
            }
        }
    }
}