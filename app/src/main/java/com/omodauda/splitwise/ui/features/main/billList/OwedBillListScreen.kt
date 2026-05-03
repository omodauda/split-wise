package com.omodauda.splitwise.ui.features.main.billList

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import com.omodauda.splitwise.data.network.model.OwedBill
import com.omodauda.splitwise.ui.components.AppTextField
import com.omodauda.splitwise.ui.features.main.friends.FriendListPlaceholder
import com.omodauda.splitwise.ui.features.main.friends.FriendPlaceholder
import com.omodauda.splitwise.ui.features.main.home.BillsViewModel
import com.omodauda.splitwise.ui.theme.ComponentDimensions
import com.omodauda.splitwise.ui.theme.CurrencySmall
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.black
import com.omodauda.splitwise.ui.theme.emerald_200
import com.omodauda.splitwise.ui.theme.emerald_50
import com.omodauda.splitwise.utils.formatDate
import com.omodauda.splitwise.utils.formatFromCents
import java.util.Date

enum class BillSortOption(val label: Int) {
//    AMOUNT_HIGH_TO_LOW(R.string.sort_high),
//    AMOUNT_LOW_TO_HIGH(R.string.sort_low),
    ASC(R.string.sort_asc),
    DESC(R.string.sort_desc),
    MOST_RECENT(R.string.sort_most_recent)
}
@Composable
fun OwedBillListScreen(
    viewModel: BillsViewModel,
    goBack: () -> Unit,
    goToBillDetails: (billId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val bills = viewModel.paginatedOwedBills.collectAsLazyPagingItems()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchQuery by viewModel.owedBillSearchQuery.collectAsStateWithLifecycle()
    val selectedSort by viewModel.owedSort.collectAsStateWithLifecycle()

    val title = stringResource(R.string.bill_title_owed)
    val totalAmount = uiState.billDashboard?.totalOwed ?: 0

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            BillListHeader(
                title = title,
                totalAmount = totalAmount,
                goBack = goBack,
                paddingTop = innerPadding.calculateTopPadding(),
                searchQuery = searchQuery,
                onSearchChanged = {viewModel.onOwedBillSearchQueryChanged(it)},
                selectedSort = selectedSort,
                onSortChanged = { viewModel.onOwedSortChanged(it) }
            )
            OwedBillList(
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
fun OwedBillList(
    bills: LazyPagingItems<OwedBill>,
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
                            OwedBillListItem(
                                personName = bill.user.fullName,
                                remainingAmount = formatFromCents(remainder),
                                descriptionText = bill.bill.description,
                                date = bill.createdAt,
                                modifier = Modifier
                                    .clickable(enabled = true, onClick = {onClick(bill.id)}),

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
fun BillListHeader(
    title: String,
    totalAmount: Int,
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
                    text = title,
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${formatFromCents(totalAmount)} ${stringResource(R.string.total)}",
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
//            Row(
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.spacedBy(Spacing.small),
//                modifier = Modifier
//                    .border(width = 1.5.dp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f), shape = MaterialTheme.shapes.large)
//                    .background(emerald_50)
//                    .padding(vertical = 12.dp, horizontal = 16.dp)
//            ) {
//                Icon(
//                    painter = painterResource(R.drawable.arrow_down),
//                    contentDescription = null,
//                    tint = MaterialTheme.colorScheme.primary
//                )
//                Text(
//                    text = "0",
//                    style = MaterialTheme.typography.titleSmall,
//                    color = MaterialTheme.colorScheme.primary
//                )
//            }
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
fun EmptyBillSearchView(
    modifier: Modifier = Modifier
){
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = Spacing.massive)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.arrow_down),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(40.dp)
            )
        }
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = stringResource(R.string.empty_search_bill_title),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
            color = black
        )
        Spacer(Modifier.height(Spacing.small))
        Text(
            text = stringResource(R.string.empty_search_bill_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = black
        )
    }
}

@Composable
fun SortButton(
    onClick: () -> Unit,
    sortTitle: Int?,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = {onClick()},
        border = BorderStroke(width = 1.dp, color = MaterialTheme.colorScheme.outlineVariant),
        shape = MaterialTheme.shapes.large,
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.background,
            contentColor = MaterialTheme.colorScheme.onBackground
        ),
        modifier = modifier
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
            Icon(
                painter = painterResource(R.drawable.sort_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = if (sortTitle !== null) stringResource(sortTitle) else stringResource(R.string.sort),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onBackground
            )
            Icon(
                painter = painterResource(R.drawable.caret_down),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

    }
}

@Composable
fun SortDropdownMenu(
    expanded: Boolean,
    selectedSort: BillSortOption?,
    onSortChanged: (BillSortOption) -> Unit,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismiss() },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.small)
    ) {
        BillSortOption.entries.forEach { option ->
            val isSelected = option == selectedSort
            DropdownMenuItem(
                text = { Text(text = stringResource(option.label), style = MaterialTheme.typography.bodyMedium)},
                colors = MenuDefaults.itemColors(
                    textColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                    trailingIconColor = MaterialTheme.colorScheme.primary
                ),
                trailingIcon = if (isSelected) {
                    {
                        Icon(
                            painter = painterResource(R.drawable.check_icon),
                            contentDescription = null
                        )
                    }
                } else null,
                onClick = {
                    onSortChanged(option)
                    onDismiss()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        if (isSelected) emerald_50 else MaterialTheme.colorScheme.background
                    )
            )
        }
    }
}

@Composable
private fun OwedBillListItem(
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
            .fillMaxWidth()
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
                    .background(color = emerald_200)
            ) {
                Text(
                    text = avatarText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
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
                    color = MaterialTheme.colorScheme.primary
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