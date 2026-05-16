package com.omodauda.splitwise.ui.features.main.confirmPayment

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.omodauda.splitwise.R
import com.omodauda.splitwise.ui.components.AppIconTextButton
import com.omodauda.splitwise.ui.components.LoadingView
import com.omodauda.splitwise.ui.features.main.billDetails.ErrorView
import com.omodauda.splitwise.ui.features.main.home.components.AvatarView
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.crystalPeak
import com.omodauda.splitwise.ui.theme.emerald_300
import com.omodauda.splitwise.ui.theme.emerald_50
import com.omodauda.splitwise.ui.theme.spectrumBlue
import com.omodauda.splitwise.utils.formatFromCents
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class PendingPaymentSortOption(val label: Int) {
    AMOUNT_HIGH_TO_LOW(R.string.sort_high),
    AMOUNT_LOW_TO_HIGH(R.string.sort_low),
    MOST_RECENT(R.string.sort_most_recent)
}

@Composable
fun ConfirmPaymentScreen(
    goBack: () -> Unit,
    confirmPaymentViewModel: ConfirmPaymentViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by confirmPaymentViewModel.uiState.collectAsStateWithLifecycle()
    val confirmActionState by confirmPaymentViewModel.confirmActionState.collectAsStateWithLifecycle()

    LaunchedEffect(confirmActionState) {
        if (confirmActionState is ConfirmActionState.Success) {
            confirmPaymentViewModel.resetActionState()
            goBack()
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        when (val state = uiState) {
            is ConfirmPaymentUiState.Loading -> LoadingView()
            is ConfirmPaymentUiState.Error -> ErrorView(
                message = state.message,
                onRetry = { confirmPaymentViewModel.fetchPaymentDetails() })

            is ConfirmPaymentUiState.Success -> {
                val payment = state.payment

                Scaffold(
                    bottomBar = {
                        ConfirmPaymentFooter(
                            onConfirm = { confirmPaymentViewModel.confirmPayment() }
                        )
                    },
                    modifier = Modifier.fillMaxSize()
                ) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(color = MaterialTheme.colorScheme.background)
                            .padding(
                                bottom = innerPadding.calculateBottomPadding(),
                                start = innerPadding.calculateStartPadding(
                                    LayoutDirection.Ltr
                                ),
                                end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                            )
                    ) {
                        ConfirmPaymentHeader(goBack)
                        Column(
                            verticalArrangement = Arrangement.spacedBy(Spacing.medium),
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(
                                    start = Spacing.medium,
                                    end = Spacing.medium
                                )
                                .verticalScroll(state = rememberScrollState())
                        ) {
                            Spacer(Modifier.height(Spacing.medium))
                            BillDetailsView(
                                category = payment.bill.category,
                                description = payment.bill.description,
                                totalBillAmount = payment.bill.totalAmount,
                                splitAmount = payment.split.amount
                            )
                            PaymentInfo(
                                fullName = payment.payer.fullName,
//                        email = payment.payer,
                                amountPaid = payment.amount,
                                datePaid = payment.createdAt
                            )
                            ConfirmationNote(
                                fullName = payment.payer.fullName,
                                paidAmount = formatFromCents(payment.amount)
                            )
                            Spacer(Modifier.height(Spacing.medium))
                        }

                    }
                }
            }
        }

        if (confirmActionState is ConfirmActionState.Loading) {
            LoadingView()
        }
    }
}

@Composable
fun ConfirmPaymentHeader(
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .shadow(1.dp)
            .padding(horizontal = Spacing.medium, vertical = ScreenDimensions.sectionSpacing)
    ) {
        IconButton(
            onClick = goBack
        ) {
            Icon(
                painter = painterResource(R.drawable.caret_left),
                contentDescription = "back icon",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = stringResource(R.string.confirm_payment),
            style = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun BillDetailsView(
    category: String,
    description: String,
    totalBillAmount: Int,
    splitAmount: Int,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
            .padding(Spacing.extraMedium)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.bill_details),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(color = crystalPeak, shape = RoundedCornerShape(14.dp))
            ) {
                Text(
                    text = category,
                    style = MaterialTheme.typography.labelMedium,
                    color = spectrumBlue,
                    modifier = Modifier
                        .padding(
                            vertical = Spacing.extraSmall,
                            horizontal = ScreenDimensions.itemSpacing
                        )
                )
            }
        }
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = description,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalDivider(Modifier.padding(vertical = ScreenDimensions.itemSpacing))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.total_bill_amount),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatFromCents(totalBillAmount),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall),
                modifier = Modifier
                    .weight(1f)
            ) {
                Text(
                    text = stringResource(R.string.your_share),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = formatFromCents(splitAmount),
                    style = MaterialTheme.typography.titleMedium.copy(fontSize = 18.sp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }

        }
    }
}

@Composable
fun PaymentInfo(
    fullName: String,
//    email: String,
    amountPaid: Int,
    datePaid: Date,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(1.dp, shape = MaterialTheme.shapes.large)
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
            .padding(Spacing.extraMedium)
    ) {
        Text(
            text = stringResource(R.string.payment_info),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(Modifier.height(Spacing.medium))
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
//                Text(
//                    text = email,
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
            }
        }
        HorizontalDivider(Modifier.padding(vertical = Spacing.medium))
        PaymentInfoView(
            label = stringResource(R.string.amount_paid),
            value = formatFromCents(amountPaid),
            valueColor = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        val formatter = SimpleDateFormat("MMMM dd, yyyy 'at' hh:mm a", Locale.getDefault())
        PaymentInfoView(
            label = stringResource(R.string.payment_date),
            value = formatter.format(datePaid),
        )
    }
}

@Composable
fun PaymentInfoView(
    label: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = value,
            style = MaterialTheme.typography.titleSmall,
            color = if (valueColor !== null) valueColor else MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun ConfirmationNote(
    fullName: String,
    paidAmount: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
        modifier = modifier
            .fillMaxWidth()
            .background(color = emerald_50, shape = MaterialTheme.shapes.large)
            .border(width = 1.dp, color = emerald_300, shape = MaterialTheme.shapes.large)
            .padding(Spacing.medium)
    ) {
        Icon(
            painter = painterResource(R.drawable.check_circle_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary
        )
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
        ) {
            Text(
                text = stringResource(R.string.verify_payment),
                style = MaterialTheme.typography.titleSmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = stringResource(R.string.verify_desc, fullName, paidAmount),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Composable
fun ConfirmPaymentFooter(
    onConfirm: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .systemBarsPadding()
            .background(color = MaterialTheme.colorScheme.background)
            .shadow(1.dp)
            .padding(Spacing.medium)
    ) {
        AppIconTextButton(
            title = stringResource(R.string.confirm_payment),
            onClick = onConfirm,
            leadingIcon = R.drawable.check_circle_icon
        )
    }
}
