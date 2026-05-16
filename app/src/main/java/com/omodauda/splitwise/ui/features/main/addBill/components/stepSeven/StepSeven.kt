package com.omodauda.splitwise.ui.features.main.addBill.components.stepSeven

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omodauda.splitwise.R
import com.omodauda.splitwise.model.AddBillUiState
import com.omodauda.splitwise.model.SplitEntryState
import com.omodauda.splitwise.ui.components.UserAvatar
import com.omodauda.splitwise.ui.features.main.addBill.AddBillSplitMethod
import com.omodauda.splitwise.ui.theme.BalanceNegative
import com.omodauda.splitwise.ui.theme.ComponentDimensions
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.SplitWiseTheme
import com.omodauda.splitwise.ui.theme.crystalPeak
import com.omodauda.splitwise.ui.theme.emerald_50
import com.omodauda.splitwise.ui.theme.hermes
import com.omodauda.splitwise.ui.theme.zumthor
import com.omodauda.splitwise.utils.formatAsCurrency
import com.omodauda.splitwise.utils.formatDate
import java.util.Date
import java.util.Locale

@Composable
fun StepSeven(
    uiState: AddBillUiState,
    currentUserId: String?,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(start = Spacing.large, end = Spacing.large)
            .verticalScroll(rememberScrollState())
    ) {
        ReviewHeader()
        Spacer(Modifier.height(Spacing.large))
        ReviewDetail(
            billAmount = uiState.billAmountAsDouble,
            description = uiState.description,
            category = uiState.category!!,
            date = uiState.date,
            paidBy = uiState.participants.find { it.userId == uiState.paidByUserId }?.let {
                if (it.userId == currentUserId) "You" else it.fullName
            } ?: "",
            splitMethod = uiState.splitMethod,
            modifier = Modifier
                .widthIn(max = 500.dp)
        )
        Spacer(Modifier.height(Spacing.medium))
        ReviewBreakDown(breakDowns = uiState.splitEntries, currentUserId = currentUserId, modifier = Modifier.widthIn(max = 500.dp))
        Spacer(Modifier.height(Spacing.medium))
        NextStepView(modifier = Modifier.widthIn(max = 500.dp))
    }
}

@Composable
fun ReviewHeader(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.review_logo),
            contentDescription = null
        )
        Text(
            text = stringResource(R.string.almost_there),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.extraSmall))
        Text(
            text = stringResource(R.string.review_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun ReviewDetail(
    billAmount: Double,
    description: String,
    category: String,
    date: Date?,
    paidBy: String,
    splitMethod: AddBillSplitMethod,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
//            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background, shape = SplitWiseShapes.dialog)
            .border(width = ComponentDimensions.borderWidthMedium, color = MaterialTheme.colorScheme.surfaceVariant, shape = SplitWiseShapes.dialog)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp))
                .padding(Spacing.large)
        ) {
            Text(
                text = stringResource(R.string.total_amount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.height(Spacing.extraSmall))
            Text(
                text = formatAsCurrency(billAmount),
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Column(
            verticalArrangement = Arrangement.spacedBy(ScreenDimensions.contentPadding),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background, shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp))
                .padding(Spacing.large)
        ) {
            ReviewDetailItem(label = R.string.add_bill_desc, value = description)
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                .fillMaxWidth()
            ) {
                ReviewDetailItem(label = R.string.category, value = category)
                ReviewDetailItem(label = R.string.date, value = formatDate(date))
            }
            ReviewDetailItem(label = R.string.paid_by, value = paidBy)
            ReviewDetailItem(label = R.string.split_method, value = stringResource(splitMethod.title))
        }
    }
}

@Composable
fun ReviewDetailItem(
    label: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(Spacing.extraSmall))
        Text(
            text = value,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ReviewBreakDown(
    currentUserId: String?,
    breakDowns: List<SplitEntryState>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier
    ) {
        Text(
            text = stringResource(R.string.split_breakdown),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))

        breakDowns.forEach {
            SplitBreakDownItem(state = it, isMe = it.user.userId === currentUserId)
            Spacer(Modifier.height(Spacing.small))
        }
    }
}

@Composable
fun SplitBreakDownItem(
    state: SplitEntryState,
    isMe: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.large)
            .border(width = ComponentDimensions.borderWidthMedium, color = MaterialTheme.colorScheme.surfaceVariant, shape = MaterialTheme.shapes.large)
            .padding(Spacing.medium)

    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
            UserAvatar(fullName = state.user.fullName, avatarUrl = state.user.avatar)
            Column{
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small)
                ) {
                    Text(
                        text = if (isMe) "You" else state.user.fullName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (isMe) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .background(color = emerald_50, shape = SplitWiseShapes.dialog)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Me",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }

                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${String.format(Locale.US, "%.2f", state.percentage)}% ${stringResource(R.string.of)} ${stringResource(R.string.total)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = formatAsCurrency(state.amount),
            style = BalanceNegative,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun NextStepView(
    modifier: Modifier = Modifier
) {
    val nextSteps = stringArrayResource(R.array.next_items)
    Column(
        modifier = modifier
//            .fillMaxWidth()
            .background(color = zumthor, shape = SplitWiseShapes.card)
            .border(width = ComponentDimensions.borderWidthMedium, color = crystalPeak, shape = SplitWiseShapes.card)
            .padding(ScreenDimensions.contentPadding)
    ) {
        Text(
            text = stringResource(R.string.what_next),
            style = MaterialTheme.typography.titleSmall,
            color = hermes
        )
        Spacer(Modifier.height(Spacing.small))
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small)
        ) {
            nextSteps.forEach { stepText ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small),
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "•",
                        style = MaterialTheme.typography.bodyMedium,
                        color = hermes
                    )
                    Text(
                        text = stepText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = hermes
                    )
                }
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun StepSevenPreview() {
    SplitWiseTheme {
        StepSeven(uiState = AddBillUiState(), currentUserId = "")
    }
}