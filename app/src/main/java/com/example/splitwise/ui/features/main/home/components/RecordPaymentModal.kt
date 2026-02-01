package com.example.splitwise.ui.features.main.home.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppTextButton
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.emerald_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordPaymentModal(
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
            RecordPaymentModalHeader(onDismiss = {onDismissRequest()})
            RecordPaymentModalContent()
        }
    }
}

@Composable
fun RecordPaymentModalHeader(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = Spacing.large, end = Spacing.large, bottom = Spacing.extraSmall)
    ) {
        Text(
            text = "Record Payment",
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        IconButton(
            onClick = {onDismiss()}
        ) {
            Icon(
                painter = painterResource(R.drawable.close_icon),
                contentDescription = "close icon"
            )
        }
    }
    HorizontalDivider(thickness = 0.4.dp, color = MaterialTheme.colorScheme.onSurface)
}

@Composable
fun RecordPaymentModalContent(
    modifier: Modifier = Modifier
) {
    var settlementAmount by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(Spacing.large)
    ) {
        OwesView()
        Spacer(Modifier.height(ScreenDimensions.sectionSpacing))
        AppTextField(
            label = "Settlement Amount",
            placeholder = "0.00",
            value = settlementAmount, // 2. Use the state variable
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
        AppTextField(
            label = "Payment method",
            placeholder = "",
            value = "",
            onValueChange = {},
            leadingIcon = R.drawable.group_icon
        )
        Spacer(Modifier.height(ScreenDimensions.sectionSpacing))
        AppTextButton(
            title = "Record Payment Received",
            onClick = {}
        )
    }
}

@Composable
fun OwesView(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = MaterialTheme.shapes.large
            )
            .padding(ScreenDimensions.itemSpacing)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(color = emerald_50, shape = CircleShape)
            ) {
                Text(
                    text = "Y",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column {
                Text(
                    text = "Sarah Johnson",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "owes you",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Text(
            text = "$130.00",
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.primary
        )
    }
}