package com.example.splitwise.ui.features.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppDropdownPicker
import com.example.splitwise.ui.components.AppTextButton
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettleUpModal(
    sheetState: SheetState,
    onDismissRequest: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = {onDismissRequest()},
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = SplitWiseShapes.bottomSheet
                )
        ) {
            RecordPaymentModalHeader(onDismiss = {onDismissRequest()}, title = R.string.settle_up)
            SettleUpModalContent()
        }
    }
}

@Composable
fun SettleUpModalContent(
    modifier: Modifier = Modifier
) {
    var settlementAmount by remember { mutableStateOf("") }
    val paymentMethods = listOf(R.string.cash, R.string.credit_card, R.string.bank_transfer)
    var selectedPaymentMethod by remember { mutableStateOf(paymentMethods[0]) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(Spacing.large)
    ) {
        PaymentDetailView(
            title = R.string.you_owe,
            amount = "$300.00",
            fullName = "David Brown",
            owe = true
        )
        Spacer(Modifier.height(ScreenDimensions.sectionSpacing))
        AppTextField(
            label = stringResource(R.string.settlement_amount),
            placeholder = "0.00",
            value = settlementAmount,
            onValueChange = { newValue ->
                val regex = """^\d*(\.\d{0,2})?$""".toRegex()
                if (newValue.matches(regex)) {
                    settlementAmount = newValue
                }
            },
            leadingIcon = R.drawable.dollar_icon,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number
            )
        )
        AppDropdownPicker(
            label = R.string.payment_method,
            options = paymentMethods,
            onOptionSelected = { selectedPaymentMethod = it },
            selectedOption = selectedPaymentMethod
        )
        Spacer(Modifier.height(ScreenDimensions.sectionSpacing))
        AppTextButton(
            title = stringResource(R.string.confirm_payment),
            onClick = {}
        )
    }
}