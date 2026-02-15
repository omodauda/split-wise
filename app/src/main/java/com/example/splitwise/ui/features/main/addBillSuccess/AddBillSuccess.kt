package com.example.splitwise.ui.features.main.addBillSuccess

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.features.main.addBill.AddBillViewModel
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.crystalPeak
import com.example.splitwise.ui.theme.emerald_50
import com.example.splitwise.utils.formatAsCurrency

@Composable
fun AddBillSuccessScreen(
    goHome: () -> Unit,
    addBillViewModel: AddBillViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by addBillViewModel.uiState.collectAsState()

    BackHandler(enabled = true) { }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(top = Spacing.large, start = Spacing.large, end = Spacing.large)
    ) {
        Image(
            painter = painterResource(R.drawable.review_logo),
            contentDescription = null,
            contentScale = ContentScale.Fit
        )
        Text(
            text = stringResource(R.string.bill_added),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        Text(
            text = stringResource(R.string.bill_success_desc, formatAsCurrency(uiState.billAmountAsDouble)),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Text(
            text = stringResource(R.string.notify_people, uiState.participants.size),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Spacing.medium))
        SuccessItem(
            logo = R.drawable.check_circle_icon,
            title = R.string.balances_update,
            desc = R.string.account_synced,
            color = emerald_50
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        SuccessItem(
            logo = R.drawable.group_icon,
            title = R.string.notifications_sent,
            desc = R.string.everyone_notified,
            color = crystalPeak
        )
        Spacer(Modifier.height(Spacing.extraLarge))
        Button(
            onClick = {goHome()},
            contentPadding = PaddingValues(Spacing.medium),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.background,
                contentColor = MaterialTheme.colorScheme.onBackground,
            ),
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.outlineVariant)
        ) {
            Icon(
                painter = painterResource(R.drawable.home_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.width(ScreenDimensions.itemSpacing))
            Text(
                text = stringResource(R.string.back_to_home),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun SuccessItem(
    logo: Int,
    title: Int,
    desc: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.large)
            .border(width = 1.dp, shape = MaterialTheme.shapes.large, color = MaterialTheme.colorScheme.outlineVariant)
            .padding(Spacing.extraMedium)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(40.dp)
                .background(color = color, shape = CircleShape)
        ) {
            Image(
                painter = painterResource(logo),
                contentDescription = null,
            )
        }
        Column {
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stringResource(desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AddBillSuccessPreview() {
    val vm = AddBillViewModel()
    SplitWiseTheme {
        AddBillSuccessScreen(goHome = {}, addBillViewModel = vm)
    }
}