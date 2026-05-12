package com.omodauda.splitwise.ui.features.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.PendingPayment
import com.omodauda.splitwise.ui.components.AppTextButton
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmActionState
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentViewModel
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Shapes
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.emerald_200
import com.omodauda.splitwise.utils.formatFromCents
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun PendingPaymentConfirmationDialog(
    pendingPayment: PendingPayment,
    confirmPaymentViewModel: ConfirmPaymentViewModel,
    onDismiss: () -> Unit,
) {
    val confirmActionState by confirmPaymentViewModel.confirmActionState.collectAsStateWithLifecycle()
    val isLoading = confirmActionState is ConfirmActionState.Loading

    LaunchedEffect(confirmActionState) {
        if (confirmActionState is ConfirmActionState.Success) {
            confirmPaymentViewModel.resetActionState()
            onDismiss()
        }
    }

    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(horizontal = Spacing.medium),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = SplitWiseShapes.dialog
                    )
                    .padding(Spacing.medium)
            ) {
                IconButton(
                    onClick = { onDismiss() },
                    enabled = !isLoading,
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.close_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(64.dp)
                        .background(color = emerald_200, shape = CircleShape)
                        .align(Alignment.CenterHorizontally)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.check_circle_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .size(32.dp)
                    )
                }
                Spacer(Modifier.height(Spacing.medium))
                Text(
                    text = stringResource(R.string.payment_confirmation),
                    style = MaterialTheme.typography.headlineSmall.copy(fontSize = 20.sp),
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(Spacing.small))
                Text(
                    text = stringResource(R.string.await_alert_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                )
                Spacer(Modifier.height(Spacing.medium))
                PaymentDetailView(
                    fullName = pendingPayment.payer.fullName,
                    amountPaid = pendingPayment.amount,
                    description = pendingPayment.bill.description,
                    category = pendingPayment.bill.category,
                    datePaid = pendingPayment.createdAt,
                )
                Spacer(Modifier.height(Spacing.medium))
                AppTextButton(
                    title = stringResource(R.string.review),
                    isLoading = isLoading,
                    onClick = { confirmPaymentViewModel.confirmPayment() }
                )
                Spacer(Modifier.height(ScreenDimensions.itemSpacing))
                AppTextButton(
                    title = stringResource(R.string.review_later),
                    onClick = { onDismiss() },
                    enabled = !isLoading,
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .border(
                            width = 1.dp,
                            color = MaterialTheme.colorScheme.outline,
                            shape = SplitWiseShapes.button
                        )
                )
            }
        }
    }
}

@Composable
fun PaymentDetailView(
    fullName: String,
    amountPaid: Int,
    description: String,
    category: String,
    datePaid: Date,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface, shape = Shapes.large)
            .padding(Spacing.medium)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AvatarView(avatarText = fullName[0].toString())
                Column(
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    Text(
                        text = fullName,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Text(
                        text = stringResource(R.string.paid_share),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            Text(
                text = formatFromCents(amountPaid),
                style = MaterialTheme.typography.titleLarge.copy(fontSize = 18.sp),
                color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        PaymentDetailEntry(label = R.string.bill, value = description)
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        PaymentDetailEntry(label = R.string.category, value = category)
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        val formatter = SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
        PaymentDetailEntry(label = R.string.payment_date, value = formatter.format(datePaid))
    }
}

@Composable
fun PaymentDetailEntry(
    label: Int,
    value: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = stringResource(label),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = value,
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.SemiBold),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}