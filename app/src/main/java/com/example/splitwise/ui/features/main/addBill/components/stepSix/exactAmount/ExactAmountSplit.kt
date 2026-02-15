package com.example.splitwise.ui.features.main.addBill.components.stepSix.exactAmount

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.model.SplitEntryState
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.features.main.addBill.components.stepSix.percentage.BillSplitNote
import com.example.splitwise.ui.features.main.addBill.components.stepSix.percentage.PersonDetail
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.crystalBlue
import com.example.splitwise.ui.theme.spectrumBlue
import com.example.splitwise.ui.theme.zumthor
import com.example.splitwise.utils.formatAsCurrency
import java.util.Locale
import kotlin.math.absoluteValue

@Composable
fun ExactAmountSplit(
    splitEntries: List<SplitEntryState>,
    billAmount: Double,
    onExactAmountChange: (userId: String, newAmount: String) -> Unit,
    onDistributeAmountEvenly: () -> Unit,
    sumOfSplitAmounts: Double,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        contentPadding = PaddingValues(bottom = ScreenDimensions.itemSpacing)
    ) {
        item {
            Button(
                onClick = {onDistributeAmountEvenly()},
                shape = SplitWiseShapes.button,
                border = BorderStroke(width = ComponentDimensions.borderWidthMedium, color = crystalBlue),
                colors = ButtonDefaults.buttonColors(
                    containerColor = zumthor
                ),
                contentPadding = PaddingValues(vertical = ScreenDimensions.itemSpacing),
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.distribute_evenly),
                    style = MaterialTheme.typography.titleMedium,
                    color = spectrumBlue
                )
            }
            Spacer(Modifier.height(Spacing.medium))
        }
        item {
            Text(
                text = stringResource(R.string.set_perc_for_each),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        items(splitEntries) { entryState ->
            AmountEntry(
                state = entryState,
                onAmountChange = {userId, newAmount ->
                    onExactAmountChange(userId, newAmount)
                }
            )
        }
        val tolerance = 0.01
        if ((sumOfSplitAmounts - billAmount).absoluteValue > tolerance) {
            item {
                BillSplitNote(note = stringResource(R.string.amount_must_add_up, formatAsCurrency(billAmount)))
            }
        }
    }
}

@Composable
fun AmountEntry(
    state: SplitEntryState,
    onAmountChange: (userId: String, newAmount: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.large)
            .border(width = ComponentDimensions.borderWidthMedium, color = MaterialTheme.colorScheme.outlineVariant, shape = MaterialTheme.shapes.large)
            .padding(ScreenDimensions.contentPadding)
    ) {
        PersonDetail(user = state.user)
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        Row(
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppTextField(
                label = stringResource(R.string.amount),
                value = String.format(Locale.US,"%.2f", state.amount),
                onValueChange = {newValue ->
                    if((newValue.toDoubleOrNull() !== null) || newValue.isEmpty()) {
                        onAmountChange(state.user.id, newValue)
                    }},
                placeholder = "200.00",
                leadingIcon = R.drawable.dollar_icon,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                modifier = Modifier
                    .weight(1f)
            )
            AppTextField(
                label = stringResource(R.string.percentage),
                value = String.format(Locale.US, "%.2f", state.percentage),
                readOnly = true,
                enabled = false,
                onValueChange = {},
                placeholder = "0.0",
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.percentage_icon),
                        contentDescription = "percentage icon"
                    )
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ExactAmountSplitPreview() {
    SplitWiseTheme {
        ExactAmountSplit(
            splitEntries = emptyList(),
            billAmount = 600.0,
            onExactAmountChange = {_, _ ->},
            onDistributeAmountEvenly = {},
            sumOfSplitAmounts = 400.0
        )
    }
}