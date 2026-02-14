package com.example.splitwise.ui.features.main.addBill.components.stepSix.percentage

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.model.SplitEntryState
import com.example.splitwise.model.User
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.acorn
import com.example.splitwise.ui.theme.butteryWhite
import com.example.splitwise.ui.theme.clearYellow
import com.example.splitwise.ui.theme.crystalBlue
import com.example.splitwise.ui.theme.emerald_50
import com.example.splitwise.ui.theme.spectrumBlue
import com.example.splitwise.ui.theme.zumthor
import java.util.Locale
import kotlin.math.absoluteValue


@Composable
fun PercentageSplit(
    splitEntries: List<SplitEntryState>,
    onPercentageChange: (userId: String, newPercentage: String) -> Unit,
    onDistributePercEvenly: () -> Unit,
    sumOfSplitPercentages: Double,
    modifier: Modifier = Modifier
) {

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        contentPadding = PaddingValues(bottom = ScreenDimensions.itemSpacing)
    ) {
        item {
            Button(
                onClick = {onDistributePercEvenly()},
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
        itemsIndexed(splitEntries) {index, entryState ->
            PercentageEntry(
                state = entryState,
                onPercentageChange = {userId, newPercentage -> onPercentageChange(userId, newPercentage)}
            )
        }
        val tolerance = 0.01
        if ((sumOfSplitPercentages - 100.0).absoluteValue > tolerance) {
            item {
                BillSplitNote(stringResource(R.string.perc_must_add_up))
            }
        }
    }
}

@Composable
fun BillSplitNote(
    note: String,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(color = butteryWhite, shape = MaterialTheme.shapes.large)
            .border(width = ComponentDimensions.borderWidthThin, color = clearYellow, shape = MaterialTheme.shapes.large)
            .padding(ScreenDimensions.contentPadding)
    ) {
        Text(
            text = note,
            style = MaterialTheme.typography.bodyMedium,
            color = acorn
        )
    }
}

@Composable
fun PercentageEntry(
    state: SplitEntryState,
    onPercentageChange: (userId: String, newPercentage: String) -> Unit,
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
                label = stringResource(R.string.percentage),
                value = String.format(Locale.US, "%.2f", state.percentage),
                onValueChange = { newValue ->
                    if((newValue.toDoubleOrNull() !== null && newValue.toDoubleOrNull()!! <= 100.0) || newValue.isEmpty()) {
                        onPercentageChange(state.user.id, newValue)
                    }
                },
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
            AppTextField(
                label = stringResource(R.string.amount),
                value = String.format(Locale.US,"%.2f", state.amount),
                onValueChange = {},
                readOnly = true,
                enabled = false,
                placeholder = "200.0",
                leadingIcon = R.drawable.dollar_icon,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}

@Composable
fun PersonDetail(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(ComponentDimensions.avatarSizeMedium)
                .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = CircleShape)
        ) {
            Text(
                text = user.name[0].toString().uppercase(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = user.name,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        if (user.name == "You") {
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
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PercentageSplitPreview() {
    SplitWiseTheme {
        PercentageSplit(
            splitEntries = emptyList(),
            onPercentageChange = {_, _ ->},
            onDistributePercEvenly = {},
            sumOfSplitPercentages = 60.0
        )
    }
}