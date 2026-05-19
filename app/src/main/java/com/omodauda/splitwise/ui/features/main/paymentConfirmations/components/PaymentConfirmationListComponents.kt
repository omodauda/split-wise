package com.omodauda.splitwise.ui.features.main.paymentConfirmations.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.itemKey
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.PendingPayment
import com.omodauda.splitwise.ui.components.AppTextField
import com.omodauda.splitwise.ui.features.main.billList.SortButton
import com.omodauda.splitwise.ui.features.main.billList.SortDropdownMenu
import com.omodauda.splitwise.ui.features.main.confirmPayment.PendingPaymentSortOption
import com.omodauda.splitwise.ui.features.main.friends.FriendListPlaceholder
import com.omodauda.splitwise.ui.features.main.friends.FriendPlaceholder
import com.omodauda.splitwise.ui.features.main.home.components.AvatarView
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.black
import com.omodauda.splitwise.ui.theme.crystalPeak
import com.omodauda.splitwise.ui.theme.emerald_50
import com.omodauda.splitwise.utils.formatFromCents
import java.text.SimpleDateFormat
import java.util.Date

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentConfirmationList(
    payments: LazyPagingItems<PendingPayment>,
    onRefresh: () -> Unit,
    searchQuery: String?,
    onClick: (paymentId: String) -> Unit,
    modifier: Modifier = Modifier,
    selectedPaymentId: String? = null
) {
    val loadState = payments.loadState
    val isRefreshing = payments.loadState.refresh is LoadState.Loading

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (loadState.refresh is LoadState.Loading) {
            FriendListPlaceholder()
        } else if (!searchQuery.isNullOrBlank() && payments.itemCount == 0) {
            EmptyPaymentSearchView()
        } else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh()},
                modifier = modifier.fillMaxSize()
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
                    horizontalAlignment = Alignment.CenterHorizontally,
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
                        count = payments.itemCount,
                        key = payments.itemKey { it.id }) { index ->
                        val payment = payments[index]
                        if (payment !== null) {
                            PaymentConfirmationItem(
                                fullName = payment.payer.fullName,
                                amountPaid = payment.amount,
                                datePaid = payment.createdAt,
                                description = payment.bill.description,
                                category = payment.bill.category,
                                isSelected = payment.id == selectedPaymentId,
                                modifier = Modifier
                                    .widthIn(max= 500.dp)
                                    .clickable(onClick = {onClick(payment.id)})
                            )
                        } else {
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
    onSortChanged: (PendingPaymentSortOption) -> Unit,
    totalCount: Int,
    modifier: Modifier = Modifier,
    selectedSort: PendingPaymentSortOption? = null,
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
                    text = "$totalCount ${stringResource(R.string.pending)}",
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
            options = PendingPaymentSortOption.entries,
            selectedSort = selectedSort,
            onSortChanged = {onSortChanged(it)},
            onDismiss = {expanded = false},
            labelProvider = { it.label }
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
    modifier: Modifier = Modifier,
    isSelected: Boolean = false,
) {
    Column(
        modifier = modifier
            .shadow(2.dp, shape = RoundedCornerShape(14.dp))
            .background(color = if (isSelected) emerald_50 else MaterialTheme.colorScheme.background, shape = RoundedCornerShape(14.dp))
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

@Composable
fun EmptyPaymentSearchView(
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
            text = stringResource(R.string.no_pending_confirmations),
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
