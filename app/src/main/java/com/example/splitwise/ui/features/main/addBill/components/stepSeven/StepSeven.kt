package com.example.splitwise.ui.features.main.addBill.components.stepSeven

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
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
import com.example.splitwise.R
import com.example.splitwise.model.AddBillUiState
import com.example.splitwise.model.SplitEntryState
import com.example.splitwise.ui.features.main.addBill.AddBillSplitMethod
import com.example.splitwise.ui.theme.BalanceNegative
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.crystalPeak
import com.example.splitwise.ui.theme.emerald_50
import com.example.splitwise.ui.theme.hermes
import com.example.splitwise.ui.theme.zumthor
import com.example.splitwise.utils.formatDate
import java.util.Date
import java.util.Locale

@Composable
fun StepSeven(
    uiState: AddBillUiState,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = Spacing.large, start = Spacing.large, end = Spacing.large, bottom = Spacing.large)
            .verticalScroll(rememberScrollState())
    ) {
        ReviewHeader()
        Spacer(Modifier.height(Spacing.large))
        ReviewDetail(
            billAmount = uiState.billAmount,
            description = uiState.description,
            category = uiState.category ?: 0,
            date = uiState.date,
            paidBy = (uiState.participants.find { it.id == uiState.paidByUserId }?.name ?: ""),
            splitMethod = uiState.splitMethod
        )
        Spacer(Modifier.height(Spacing.medium))
        ReviewBreakDown(breakDowns = uiState.splitEntries)
        Spacer(Modifier.height(Spacing.medium))
        NextStepView()
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
    category: Int,
    date: Date?,
    paidBy: String,
    splitMethod: AddBillSplitMethod,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
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
                text = "$${String.format(Locale.US, "%.2f", billAmount)}",
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
                ReviewDetailItem(label = R.string.category, value = stringResource(category))
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
            SplitBreakDownItem(state = it)
            Spacer(Modifier.height(Spacing.small))
        }
    }
}

@Composable
fun SplitBreakDownItem(
    state: SplitEntryState,
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
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(ComponentDimensions.iconSizeExtraLarge)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
            ) {
                Text(
                    text = state.user.name[0].uppercase(),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column{
                Row(
                    horizontalArrangement = Arrangement.spacedBy(Spacing.small)
                ) {
                    Text(
                        text = state.user.name,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    if (state.user.name == "You") {
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
                    text = "${state.percentage}% ${stringResource(R.string.of)} ${stringResource(R.string.total)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Text(
            text = "$${String.format(Locale.US, "%.2f", state.amount)}",
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
            .fillMaxWidth()
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
        StepSeven(uiState = AddBillUiState())
    }
}