package com.example.splitwise.ui.features.main.addBill.components.stepFour

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.model.User
import com.example.splitwise.ui.features.main.addBill.components.stepTwo.components.Friend
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.utils.formatAsCurrency

@Composable
fun StepFour(
    billAmount: Double,
    onPayerSelected: (String) -> Unit,
    participants: List<User>,
    modifier: Modifier = Modifier,
    payerId: String? = null,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Spacing.large)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.primary, shape = SplitWiseShapes.dialog)
                .padding(Spacing.large)
        ) {
            Text(
                text = stringResource(R.string.bill_amount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.height(Spacing.extraSmall))
            Text(
                text = formatAsCurrency(billAmount),
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = stringResource(R.string.who_paid_bill),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.medium))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            contentPadding = PaddingValues(bottom = Spacing.extraMedium)
        ) {
            items(participants) {user ->
                val isSelected = user.id == payerId
                Friend(
                    user,
                    isSelected,
                    modifier = Modifier
                        .clickable {
                            onPayerSelected(user.id)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun StepFourPreview() {
    SplitWiseTheme {
        StepFour(
            billAmount = 0.00,
            payerId = null,
            onPayerSelected = {},
            participants = emptyList()
        )
    }
}