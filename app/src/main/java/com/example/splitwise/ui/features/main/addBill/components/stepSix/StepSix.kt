package com.example.splitwise.ui.features.main.addBill.components.stepSix

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.model.AddBillUiState
import com.example.splitwise.ui.features.main.addBill.AddBillSplitMethod
import com.example.splitwise.ui.features.main.addBill.components.stepSix.exactAmount.ExactAmountSplit
import com.example.splitwise.ui.features.main.addBill.components.stepSix.percentage.PercentageSplit
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.brightYellow
import java.util.Locale

@Composable
fun StepSix(
    uiState: AddBillUiState,
    onPercentageChange: (userId: String, newPercentage: String) -> Unit,
    onExactAmountChange: (userId: String, newAmount: String) -> Unit,
    onDistributePercentageEvenly: () -> Unit,
    onDistributeAmountEvenly: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = Spacing.large, start = Spacing.large, end = Spacing.large)
    ) {
        BalanceDetails(
            billAmount = uiState.billAmount,
            splitMethod = uiState.splitMethod,
            remainingPercentage = uiState.remainingPercentage,
            remainingBillAmount = uiState.remainingAmount
        )
        Spacer(Modifier.height(ScreenDimensions.contentPadding))
        if(uiState.splitMethod == AddBillSplitMethod.EXACT) {
            ExactAmountSplit(
                splitEntries = uiState.splitEntries,
                billAmount = uiState.billAmount,
                onExactAmountChange = {userId, newAmount -> onExactAmountChange(userId, newAmount)},
                onDistributeAmountEvenly = {onDistributeAmountEvenly()},
                sumOfSplitAmounts = uiState.sumOfSplitAmount
            )
        } else if (uiState.splitMethod == AddBillSplitMethod.PERCENTAGE) {
            PercentageSplit(
                splitEntries = uiState.splitEntries,
                onPercentageChange = {userId, newPercentage -> onPercentageChange(userId, newPercentage)},
                onDistributePercEvenly = {onDistributePercentageEvenly()},
                sumOfSplitPercentages = uiState.sumOfSplitPercentage
            )
        }
    }
}

@Composable
fun BalanceDetails(
    billAmount: Double,
    splitMethod: AddBillSplitMethod,
    remainingPercentage: Double,
    remainingBillAmount: Double,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary, shape = SplitWiseShapes.dialog)
            .padding(Spacing.large)
    ) {
        Column {
            Text(
                text = stringResource(R.string.total_amount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.height(Spacing.extraSmall))
            Text(
                text = "$$billAmount",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Column {
            Text(
                text = stringResource(R.string.remaining),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.height(Spacing.extraSmall))
            Text(
                text = if (splitMethod == AddBillSplitMethod.PERCENTAGE) "${String.format(Locale.US, "%.2f", remainingPercentage)}%" else "$${String.format(
                    Locale.US, "%.2f", remainingBillAmount)}",
                style = MaterialTheme.typography.headlineSmall,
                color = brightYellow
            )
        }
    }
}

@Preview
@Composable
fun StepSixPreview() {
    SplitWiseTheme {
        StepSix(
            uiState = AddBillUiState(),
            onPercentageChange = { _, _ -> },
            onDistributePercentageEvenly = {},
            onExactAmountChange = {_, _ ->},
            onDistributeAmountEvenly = {}
        )
    }
}