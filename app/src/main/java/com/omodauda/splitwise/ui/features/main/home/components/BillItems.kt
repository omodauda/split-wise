package com.omodauda.splitwise.ui.features.main.home.components

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
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.OwedBill
import com.omodauda.splitwise.data.network.model.OwingBill
import com.omodauda.splitwise.ui.theme.ComponentDimensions
import com.omodauda.splitwise.ui.theme.CurrencySmall
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.emerald_200
import com.omodauda.splitwise.utils.formatFromCents

@Composable
fun OwedItem(
    bill: OwedBill,
    openReminderModal: () -> Unit,
    openRecordPaymentModal: () -> Unit,
    onBillItemClicked: (billId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val remainingAmount = bill.amount - bill.paidAmount
    BillItem(
        avatarBackgroundColor = emerald_200,
        avatarContentColor = MaterialTheme.colorScheme.primary,
        personName = bill.user.fullName,
        descriptionText = stringResource(R.string.owes_you),
        billAmount = formatFromCents(bill.amount),
        paidAmount = formatFromCents(bill.paidAmount),
        remainingAmount = formatFromCents(remainingAmount),
        amountColor = MaterialTheme.colorScheme.primary,
        buttonText = R.string.settle,
        buttonContainerColor = emerald_200,
        buttonContentColor = MaterialTheme.colorScheme.primary,
        actOnBill = { openRecordPaymentModal() },
        sendReminder = { openReminderModal() },
        modifier = modifier
            .clickable(enabled = true, onClick = {onBillItemClicked(bill.bill.id)})
    )
}

@Composable
fun OwingItem(
    bill: OwingBill,
    openSettleUpModal: () -> Unit,
    onBillItemClicked: (billId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val remainingAmount = bill.amount - bill.paidAmount
    BillItem(
        avatarBackgroundColor = MaterialTheme.colorScheme.errorContainer,
        avatarContentColor = MaterialTheme.colorScheme.error,
        personName = bill.bill.paidBy.fullName,
        descriptionText = stringResource(R.string.you_owe),
        billAmount = formatFromCents(bill.amount),
        paidAmount = formatFromCents(bill.paidAmount),
        remainingAmount = formatFromCents(remainingAmount),
        amountColor = MaterialTheme.colorScheme.error,
        buttonText = R.string.pay,
        buttonContainerColor = MaterialTheme.colorScheme.errorContainer,
        buttonContentColor = MaterialTheme.colorScheme.error,
        actOnBill = { openSettleUpModal() },
        modifier = modifier
            .clickable(enabled = true, onClick = {onBillItemClicked(bill.bill.id)})
    )
}

@Composable
private fun BillItem(
    avatarBackgroundColor: Color,
    avatarContentColor: Color,
    personName: String,
    descriptionText: String,
    billAmount: String,
    paidAmount: String,
    remainingAmount: String,
    amountColor: Color,
    buttonText: Int,
    buttonContainerColor: Color,
    buttonContentColor: Color,
    actOnBill: () -> Unit,
    modifier: Modifier = Modifier,
    sendReminder: (() -> Unit)? = null,
) {
    val avatarText = personName[0].uppercase()
    Column(
        verticalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier
            .fillMaxWidth()
            .padding(ScreenDimensions.contentPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
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
                Column {
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
                Button(
                    onClick = { actOnBill() },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = buttonContainerColor,
                        contentColor = buttonContentColor
                    ),
                    contentPadding = PaddingValues(
                        vertical = Spacing.extraSmall,
                        horizontal = ScreenDimensions.itemSpacing
                    ),
                ) {
                    Text(
                        text = stringResource(buttonText),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if (sendReminder != null) {
                    Icon(
                        painter = painterResource(id = R.drawable.bell_icon),
                        contentDescription = stringResource(R.string.send_reminder),
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .clickable(
                                enabled = true,
                                onClick = { sendReminder() }
                            )
                    )
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(Spacing.extraMedium),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total: $billAmount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = "Paid: $paidAmount",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column {
                Text(
                    text = remainingAmount,
                    style = CurrencySmall,
                    color = amountColor
                )
                Text(
                    text = "remaining",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}