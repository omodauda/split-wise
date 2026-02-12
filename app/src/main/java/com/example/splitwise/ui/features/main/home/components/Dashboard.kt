package com.example.splitwise.ui.features.main.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import com.example.splitwise.R
import com.example.splitwise.ui.theme.CurrencyLarge
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.emerald_500

@Composable
fun DashBoard(
    paddingTop: Dp,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
//            .systemBarsPadding()
            .padding(top = paddingTop + ScreenDimensions.verticalPadding, start = Spacing.large, end = Spacing.large, bottom = ScreenDimensions.verticalPadding)
    ) {
        Text(
            text = stringResource(R.string.dashboard),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.height(Spacing.large))
        BalanceView(
            title = R.string.net_balance
        )
        Spacer(Modifier.height(Spacing.large))
        Row(
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            BalanceView(
                icon = R.drawable.arrow_down,
                title = R.string.total_owed,
                modifier = Modifier
                    .weight(1f)
            )
            BalanceView(
                icon = R.drawable.arrow_up,
                title = R.string.total_owing,
                modifier = Modifier
                    .weight(1f)
            )
        }
    }
}
@Composable
fun BalanceView(
    title: Int,
    modifier: Modifier = Modifier,
    icon: Int? = null,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(emerald_500, shape = SplitWiseShapes.card)
            .padding(ScreenDimensions.contentPadding)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.small),
            modifier = Modifier.fillMaxWidth()
        ) {
            if (icon != null) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = null
                )
            }
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Spacer(Modifier.height(Spacing.extraSmall))
        Text(
            text = "$0.00",
            style = CurrencyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}