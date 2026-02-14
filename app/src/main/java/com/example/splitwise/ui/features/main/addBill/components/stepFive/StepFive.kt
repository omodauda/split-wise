package com.example.splitwise.ui.features.main.addBill.components.stepFive

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.features.main.addBill.AddBillSplitMethod
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.emerald_500
import java.util.Locale


@Composable
fun StepFive(
    billAmount: Double,
    numberOfPersons: Int,
    splitMethod: AddBillSplitMethod,
    payerName: String,
    selectSplitMethod: (method: AddBillSplitMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Spacing.large)
    ) {
        PaymentDetails(
            billAmount,
            payerName
        )
        Spacer(Modifier.height(Spacing.medium))
        SplitMethodsView(billAmount = billAmount, splitMethod = splitMethod, selectSplitMethod = selectSplitMethod, numberOfPersons = numberOfPersons)
    }
}

@Composable
fun PaymentDetails(
    billAmount: Double,
    payerName: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary, shape = SplitWiseShapes.dialog)
            .padding(Spacing.large)
    ) {
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
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = emerald_500, shape = MaterialTheme.shapes.large)
                .padding(vertical = ScreenDimensions.itemSpacing, horizontal = Spacing.medium)
        ) {
            Text(
                text = stringResource(R.string.paid_by),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Text(
                text = payerName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun SplitMethodsView(
    billAmount: Double,
    numberOfPersons: Int,
    splitMethod: AddBillSplitMethod,
    selectSplitMethod: (method: AddBillSplitMethod) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.how_to_split),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.medium))
        AddBillSplitMethod.entries.forEach { method ->
            SplitMethodItem(
                method = method,
                isSelected = splitMethod == method,
                billAmount,
                numberOfPersons,
                modifier = Modifier.clickable { selectSplitMethod(method) }
            )
            Spacer(Modifier.height(Spacing.medium))
        }
    }
}

@Composable
fun SplitMethodItem(
    method: AddBillSplitMethod,
    isSelected: Boolean,
    billAmount: Double,
    numberOfPersons: Int,
    modifier: Modifier = Modifier
) {
    val exactPerPerson = billAmount / numberOfPersons
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
            .border(
                width = ComponentDimensions.borderWidthMedium,
                color = borderColor,
                shape = MaterialTheme.shapes.large
            )
            .padding(Spacing.extraMedium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.horizontalPadding),
            modifier = Modifier
                .weight(1f)
        ) {
            Box(
                modifier = Modifier
                    .background(color = method.color, shape = SplitWiseShapes.card)
                    .padding(8.dp)
            ) {
                Text(
                    text = method.icon,
                    style = MaterialTheme.typography.headlineSmall
                )
            }
            Column {
                Text(
                    text = stringResource(method.title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(Spacing.extraSmall))
                Text(
                    text = stringResource(method.description),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                if (method == AddBillSplitMethod.EQUAL) {
                    Spacer(Modifier.height(Spacing.extraSmall))
                    Text(
                        text = "$${String.format(Locale.US, "%.2f", exactPerPerson)} ${stringResource(R.string.per_person)}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
        if (isSelected) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(ComponentDimensions.iconSizeMedium)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .padding(6.dp)
            ) {
                Icon(
                    painter = painterResource(R.drawable.check_icon),
                    contentDescription = "check icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Preview
@Composable
fun StepFivePreview() {
    SplitWiseTheme {
        StepFive(
            billAmount = 400.00,
            numberOfPersons = 2,
            splitMethod = AddBillSplitMethod.EXACT,
            payerName = "You",
            selectSplitMethod = {}
        )
    }
}