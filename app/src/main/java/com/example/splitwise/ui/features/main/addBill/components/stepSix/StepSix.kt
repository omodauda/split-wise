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
import com.example.splitwise.ui.features.main.addBill.components.stepSix.percentage.PercentageSplit
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.brightYellow

@Composable
fun StepSix(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(top = Spacing.large, start = Spacing.large, end = Spacing.large)
    ) {
        BalanceDetails()
        Spacer(Modifier.height(ScreenDimensions.contentPadding))
        PercentageSplit()
    }
}

@Composable
fun BalanceDetails(
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
                text = "$600",
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
                text = "0.0%",
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
        StepSix()
    }
}