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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.model.User
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.features.main.addBill.components.stepSix.percentage.BillSplitNote
import com.example.splitwise.ui.features.main.addBill.components.stepSix.percentage.PercentageEntryState
import com.example.splitwise.ui.features.main.addBill.components.stepSix.percentage.PersonDetail
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.crystalBlue
import com.example.splitwise.ui.theme.spectrumBlue
import com.example.splitwise.ui.theme.zumthor

@Composable
fun ExactAmountSplit(
    modifier: Modifier = Modifier
) {
    val totalBillAmount = 900.0
    val users = listOf(
        User("1", "You", ""),
        User("2", "Sarah Johnson", ""),
        User("3", "Mike Chen", "")
    )

    // The single source of truth for the state of all entries
    val entryStates = remember {
        mutableStateListOf<PercentageEntryState>().apply {
            addAll(users.map { PercentageEntryState(user = it) })
        }
    }

    fun distributeEvenly() {
        if (users.isEmpty()) return

        val evenPercentage = 100.0 / users.size
        val evenAmount = totalBillAmount / users.size

        val newStates = users.map { user ->
            PercentageEntryState(
                user = user,
                percentage = String.format(java.util.Locale.US, "%.1f", evenPercentage),
                amount = evenAmount
            )
        }
        entryStates.clear()
        entryStates.addAll(newStates)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        contentPadding = PaddingValues(bottom = ScreenDimensions.itemSpacing)
    ) {
        item {
            Button(
                onClick = {distributeEvenly()},
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
        itemsIndexed(entryStates) {index, entryState ->
            AmountEntry(
                state = entryState,
                onAmountChange = {newAmount ->
                    val amountAsDouble = newAmount.toDoubleOrNull() ?: 0.0
                    val newPercentageDouble = (amountAsDouble / totalBillAmount) * 100.0
                    val newPercentageString = String.format(java.util.Locale.US, "%.2f", newPercentageDouble)
                    entryStates[index] = entryState.copy(
                        percentage = newPercentageString,
                        amount = amountAsDouble
                    )
                }
            )
        }
        item {
            BillSplitNote(note = stringResource(R.string.amount_must_add_up, totalBillAmount))
        }
    }
}

@Composable
fun AmountEntry(
    state: PercentageEntryState,
    onAmountChange: (String) -> Unit,
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
                value = String.format(java.util.Locale.US,"%.2f", state.amount),
                onValueChange = {newValue ->
                    if((newValue.toDoubleOrNull() !== null && newValue.toDoubleOrNull()!! <= 100.0) || newValue.isEmpty()) {
                        onAmountChange(newValue)
                    }},
                placeholder = "200.00",
                leadingIcon = R.drawable.dollar_icon,
                modifier = Modifier
                    .weight(1f)
            )
            AppTextField(
                label = stringResource(R.string.percentage),
                value = state.percentage,
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
        ExactAmountSplit()
    }
}