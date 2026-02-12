package com.example.splitwise.ui.features.main.addBill.components.stepFive

import androidx.annotation.StringRes
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.blue_chalk
import com.example.splitwise.ui.theme.crystalPeak
import com.example.splitwise.ui.theme.emerald_50
import com.example.splitwise.ui.theme.emerald_500
import com.example.splitwise.ui.theme.papaya_whip
enum class SplitMethod(
    val icon: String,
    @StringRes val title: Int,
    @StringRes val description: Int,
    val color: Color
) {
    EQUAL(
        icon = "⚖️",
        title = R.string.split_equally,
        description = R.string.equal_desc,
        color = crystalPeak
    ),
    PERCENTAGE(
        icon = "📊",
        title = R.string.by_percentage,
        description = R.string.perc_desc,
        color = blue_chalk
    ),
    EXACT(
        icon = "💵",
        title = R.string.exact_amounts,
        description = R.string.exact_desc,
        color = papaya_whip
    )
}

@Composable
fun StepFive(
    modifier: Modifier = Modifier
) {
    val billAmount = 600.00
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Spacing.large)
    ) {
        PaymentDetails()
        Spacer(Modifier.height(Spacing.medium))
        SplitMethodsView(billAmount = billAmount)
    }
}

@Composable
fun PaymentDetails(
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
            text = "$600",
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
                text = "You",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
fun SplitMethodsView(
    billAmount: Double,
    modifier: Modifier = Modifier
) {
    var selectedMethod by rememberSaveable { mutableStateOf(SplitMethod.EQUAL) }
    Column(
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.how_to_split),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.medium))
        SplitMethod.entries.forEach { method ->
            SplitMethodItem(
                method = method,
                isSelected = selectedMethod == method,
                billAmount,
                modifier = Modifier.clickable { selectedMethod = method } // Update state on click
            )
            Spacer(Modifier.height(Spacing.medium))
        }
    }
}

@Composable
fun SplitMethodItem(
    method: SplitMethod,
    isSelected: Boolean,
    billAmount: Double,
    modifier: Modifier = Modifier
) {
    val borderColor = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = if (isSelected) emerald_50 else MaterialTheme.colorScheme.background,
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
                val exactPerPerson = billAmount / 3
                if (method == SplitMethod.EQUAL) {
                    Spacer(Modifier.height(Spacing.extraSmall))
                    Text(
                        text = "$$exactPerPerson ${stringResource(R.string.per_person)}",
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
        StepFive()
    }
}