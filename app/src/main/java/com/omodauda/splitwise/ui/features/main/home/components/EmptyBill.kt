package com.omodauda.splitwise.ui.features.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.omodauda.splitwise.R
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing

@Composable
fun EmptyBillView(
//    onAddBill: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .clip(shape = CircleShape)
                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                .padding(vertical = ScreenDimensions.sectionSpacing, horizontal = 26.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.bill_logo),
                contentDescription = "bill logo",
                tint = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = stringResource(R.string.no_bill),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.small))
        Text(
            text = stringResource(R.string.no_bill_desc),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Spacing.large))
//        AppIconTextButton(
//            leadingIcon = R.drawable.plus_icon,
//            title = stringResource(R.string.add_first_bill),
//            onClick = {onAddBill()},
//            modifier = Modifier
//                .width(203.dp)
//        )
    }
}