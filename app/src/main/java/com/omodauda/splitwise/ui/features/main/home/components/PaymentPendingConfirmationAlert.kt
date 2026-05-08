package com.omodauda.splitwise.ui.features.main.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import com.omodauda.splitwise.R
import com.omodauda.splitwise.ui.theme.Shapes
import com.omodauda.splitwise.ui.theme.emerald_200
import com.omodauda.splitwise.ui.theme.emerald_800
import com.omodauda.splitwise.ui.theme.emerald_900
import com.omodauda.splitwise.ui.theme.teal_50

@Composable
fun PaymentPendingConfirmationAlert(
    onDismiss: () -> Unit,
    onReview: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(teal_50, shape = Shapes.large)
            .border(width = 0.6.dp, color = emerald_200, shape = Shapes.large)
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(emerald_200, shape = RoundedCornerShape(24.dp))
            ) {
                Icon(
                    painter = painterResource(R.drawable.check_circle_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(12.dp)
                )

            }
            Column{
                Text(
                    text = stringResource(R.string.alert_title, 3),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = emerald_900
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    text = stringResource(R.string.alert_desc),
                    style = MaterialTheme.typography.bodyMedium,
                    color = emerald_800
                )
                Spacer(Modifier.height(14.dp))
                TextButton(
                    onClick = {onReview()},
                    contentPadding = PaddingValues(0.dp)

                ) {
                    Text(
                        text = stringResource(R.string.review_now),
                        textDecoration = TextDecoration.Underline,
                        style = MaterialTheme.typography.titleSmall,
                        color = emerald_800
                    )
                }
            }
        }

        IconButton(
            onClick = {onDismiss()}
        ) {
            Icon(
                painter = painterResource(R.drawable.close_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
            )
        }
    }
}