package com.example.splitwise.ui.features.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.CurrencySmall
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.emerald_200

@Composable
fun OwedItem(
    openReminderModal: () -> Unit,
    openRecordPaymentModal: () -> Unit,
    modifier: Modifier = Modifier
) {
    BillItem(
        avatarBackgroundColor = emerald_200,
        avatarContentColor = MaterialTheme.colorScheme.primary,
        personName = "Sarah John",
        descriptionText = stringResource(R.string.owes_you),
        amountText = "$138.63",
        amountColor = MaterialTheme.colorScheme.primary,
        buttonText = "Settle",
        buttonContainerColor = emerald_200,
        buttonContentColor = MaterialTheme.colorScheme.primary,
        actOnBill = {openRecordPaymentModal()},
        sendReminder = {openReminderModal()},
        modifier = modifier
    )
}

@Composable
fun OwingItem(
    modifier: Modifier = Modifier
) {
    BillItem(
        avatarBackgroundColor = MaterialTheme.colorScheme.errorContainer,
        avatarContentColor = MaterialTheme.colorScheme.error,
        personName = "David Brown",
        descriptionText = stringResource(R.string.you_owe),
        amountText = "$36.44",
        amountColor = MaterialTheme.colorScheme.error,
        buttonText = "Pay",
        buttonContainerColor = MaterialTheme.colorScheme.errorContainer,
        buttonContentColor = MaterialTheme.colorScheme.error,
        actOnBill = {},
        modifier = modifier
    )
}

@Composable
private fun BillItem(
    avatarBackgroundColor: Color,
    avatarContentColor: Color,
    personName: String,
    descriptionText: String,
    amountText: String,
    amountColor: Color,
    buttonText: String,
    buttonContainerColor: Color,
    buttonContentColor: Color,
    actOnBill: () -> Unit,
    sendReminder: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    val avatarText = personName[0].uppercase()
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(ScreenDimensions.contentPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            modifier = Modifier.weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(ComponentDimensions.avatarSizeMedium)
                    .clip(shape = CircleShape)
                    .background(color = avatarBackgroundColor)
            ) {
                Text(
                    text = avatarText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = avatarContentColor
                )
            }
            Column{
                Text(
                    text = personName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground,
                    maxLines = 2
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = descriptionText,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
            Text(
                text = amountText,
                style = CurrencySmall,
                color = amountColor
            )
            Button(
                onClick = {actOnBill()},
                colors = ButtonDefaults.buttonColors(
                    containerColor = buttonContainerColor,
                    contentColor = buttonContentColor
                ),
                contentPadding = PaddingValues(vertical = Spacing.extraSmall, horizontal = ScreenDimensions.itemSpacing),
            ) {
                Text(
                    text = buttonText,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
            if (sendReminder != null) {
                Icon(
                    painter = painterResource(id = R.drawable.bell_icon),
                    contentDescription = "Send Reminder",
                    tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable(
                                enabled = true,
                                onClick = {sendReminder()}
                            )
                )
            }
        }
    }
}