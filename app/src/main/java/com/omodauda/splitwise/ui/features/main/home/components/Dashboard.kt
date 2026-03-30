package com.omodauda.splitwise.ui.features.main.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.GetBillsDashboardResponse
import com.omodauda.splitwise.ui.components.AppIconTextButton
import com.omodauda.splitwise.ui.theme.CurrencyMedium
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.emerald_500
import com.omodauda.splitwise.utils.formatFromCents
import com.valentinilk.shimmer.shimmer

@Composable
fun DashBoard(
    isLoading: Boolean,
    data: GetBillsDashboardResponse?,
    paddingTop: Dp,
    onAddBill: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(
                top = paddingTop + ScreenDimensions.verticalPadding,
                start = Spacing.large,
                end = Spacing.large,
                bottom = ScreenDimensions.verticalPadding
            )
    ) {
        Text(
            text = stringResource(R.string.dashboard),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.height(Spacing.large))
        BalanceView(
            title = R.string.net_balance,
            value = data?.netBalance ?: 0,
            isLoading = isLoading
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
                value = data?.totalOwed ?: 0,
                isLoading = isLoading,
                modifier = Modifier
                    .weight(1f)
            )
            BalanceView(
                icon = R.drawable.arrow_up,
                title = R.string.total_owing,
                value = data?.totalOwing ?: 0,
                isLoading = isLoading,
                modifier = Modifier
                    .weight(1f)
            )
        }
        Spacer(Modifier.height(Spacing.large))
        AppIconTextButton(
            leadingIcon = R.drawable.plus_icon,
            title = stringResource(R.string.add_bill),
            onClick = {onAddBill()},
            containerColor = emerald_500
        )
    }
}
@Composable
fun BalanceView(
    isLoading: Boolean,
    title: Int,
    value: Int,
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
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(28.dp)
                    .shimmer()
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.3f),
                        shape = MaterialTheme.shapes.small
                    )
            )
        } else {
            Text(
                text = formatFromCents(value),
                style = CurrencyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}