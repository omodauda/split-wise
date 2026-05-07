package com.omodauda.splitwise.ui.features.main.billDetails

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.BillDetails
import com.omodauda.splitwise.data.network.model.User
import com.omodauda.splitwise.ui.features.main.home.components.AvatarView
import com.omodauda.splitwise.ui.theme.BalanceNegative
import com.omodauda.splitwise.ui.theme.BalancePositive
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Shapes
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseTheme
import com.omodauda.splitwise.ui.theme.emerald_200
import com.omodauda.splitwise.ui.theme.emerald_50
import com.omodauda.splitwise.ui.theme.hotOrange
import com.omodauda.splitwise.utils.formatFromCents
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun BillDetailsScreen(
    currentUserId: String,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: BillDetailsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            BillDetailsHeader(
                goBack = onBackClick,
                paddingTop = innerPadding.calculateTopPadding()
            )
            when (val state = uiState) {
                is BillDetailsUiState.Loading -> LoadingView()
                is BillDetailsUiState.Error -> ErrorView(state.message, onRetry = { viewModel.fetchBillDetails() })
                is BillDetailsUiState.Success -> {
                    BillDetailsContent(currentUserId = currentUserId, bill = state.bill, paddingBottom = innerPadding.calculateBottomPadding())
                }
            }
        }
    }
}

@Composable
fun BillDetailsHeader(
    goBack: () -> Unit,
    paddingTop: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.large),
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .shadow(elevation = 1.dp)
            .padding(
                top = paddingTop + Spacing.medium,
                start = Spacing.medium,
                end = Spacing.medium,
                bottom = Spacing.medium
            )
    ) {
        IconButton(onClick = {goBack()}) {
            Icon(
                painter = painterResource(R.drawable.back_icon),
                contentDescription = "back icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = "Bill Details",
            style = BalanceNegative,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun BillDetailsContent(
    currentUserId: String,
    paddingBottom: Dp,
    bill: BillDetails,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(top = Spacing.medium, start = Spacing.medium, end = Spacing.medium)
    ) {
        item {
            BillDetailView(
                description = bill.description,
                category = bill.category,
                date = bill.date,
                totalAmount = bill.totalAmount,
                splitMethod = bill.splitMethod,
            )
            Spacer(Modifier.height(Spacing.medium))
        }
        item {
            BillPayerView(
                currentUserId = currentUserId,
                paidBy = bill.paidBy
            )
            Spacer(Modifier.height(Spacing.medium))
        }
        item {
            BillSplitBreakDownView(
                paddingBottom = paddingBottom,
                currentUserId = currentUserId,
                splits = bill.splits
            )
        }
    }
}

@Composable
fun BillDetailView(
    description: String,
    category: String,
    date: Date,
    totalAmount: Int,
    splitMethod: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp))
            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(24.dp))
            .padding(Spacing.large)
    ) {
        Box(
            modifier = Modifier
                .background(color = emerald_50, shape = Shapes.large)
                .padding(Spacing.medium)
        ) {
            Icon(
                painter = painterResource(R.drawable.activity_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(Spacing.large))
        Text(
            text = description,
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.small))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(7.dp)
        ) {
            Box(
                modifier = Modifier
                    .background(emerald_200, shape = Shapes.extraSmall)
                    .padding(vertical = Spacing.extraSmall, horizontal = Spacing.small)
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            val formatter = SimpleDateFormat("MMMM d, yyyy", Locale.getDefault())
            Text(
                text = formatter.format(date),
                style = MaterialTheme.typography.labelLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(Spacing.large))
        HorizontalDivider(Modifier.height(1.dp))
        Spacer(Modifier.height(Spacing.large))
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier
                .fillMaxWidth()

        ) {
            Column {
                Text(
                    text = "TOTAL BILL",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(Spacing.extraSmall))
                Text(
                    text = formatFromCents(totalAmount),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Column(
                horizontalAlignment = Alignment.End
            ) {
                Text(
                    text = "SPLIT METHOD",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(Modifier.height(Spacing.extraSmall))
                Text(
                    text = splitMethod,
                    style = BalancePositive,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun BillPayerView(
    currentUserId: String,
    paidBy: User,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp))
            .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(24.dp))
            .padding(Spacing.large)
    ) {
        Box(
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface, shape = Shapes.large)
                .padding(Spacing.medium)
        ) {
            Icon(
                painter = painterResource(R.drawable.dollar_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Column {
            Text(
                text = "Paid by",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = if (paidBy.id == currentUserId) "You" else paidBy.fullName,
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
fun BillSplitBreakDownView(
    paddingBottom: Dp,
    currentUserId: String,
    splits: List<com.omodauda.splitwise.data.network.model.Split>,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth().padding(bottom = paddingBottom + Spacing.medium)) {
        Text(
            text = "SPLIT BREAKDOWN",
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        Column(
            modifier = modifier
                .fillMaxWidth()
                .shadow(elevation = 1.dp, shape = RoundedCornerShape(24.dp))
                .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(24.dp))
                .padding(Spacing.large)
        ) {
            splits.forEachIndexed { index, split ->
                BillSplitBreakDown(split = split, currentUserId = currentUserId )

                if (index < splits.lastIndex) {
                    HorizontalDivider(Modifier.padding(vertical = Spacing.medium))
                }
            }
        }
    }


}

@Composable
fun BillSplitBreakDown(
    currentUserId: String,
    split: com.omodauda.splitwise.data.network.model.Split,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(Spacing.medium),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
            ) {
                val avatarText = split.user.fullName[0].toString()
                AvatarView(avatarText)
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = if (split.user.id == currentUserId) "You" else split.user.fullName,
                        style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    val isSettled = split.settled
//                    val isSettled = if (paidById === split.user.id && split.settled) true else (split.paidAmount >= split.amount)
                    Text(
                        text = if (isSettled) "Paid in full" else "Payment Pending",
                        style = MaterialTheme.typography.labelMedium,
                        color = if (isSettled) MaterialTheme.colorScheme.primary else hotOrange
                    )
                }
            }
            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                Text(
                    text = formatFromCents(split.amount),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "SPLIT SHARE",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .background(color = MaterialTheme.colorScheme.inverseOnSurface, shape = Shapes.large)
                .padding(vertical = Spacing.medium)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "Paid",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatFromCents(if (split.settled) split.amount else split.paidAmount),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.primary
                )
            }
            VerticalDivider(modifier = Modifier.height(40.dp), thickness = 1.dp, color = MaterialTheme.colorScheme.onBackground)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = "To Balance",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                val balance = if (split.settled) 0 else (split.amount - split.paidAmount)
//                val isSplitSettled = split.paidAmount >= split.amount
                Text(
                    text = formatFromCents(balance),
                    style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                    color = if (balance > 0) hotOrange else MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
fun LoadingView(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
    }
}

@Composable
fun ErrorView(
    message: String,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(Spacing.large),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(Spacing.medium))
        Button(onClick = onRetry) {
            Text(text = "Retry")
        }
    }
}

@Preview(
    name = "Light mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark mode",
    showBackground = true,
    showSystemUi = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun BillDetailsScreenPreview() {
    SplitWiseTheme {
        BillDetailsScreen(currentUserId = "1", onBackClick = {})
    }
}
