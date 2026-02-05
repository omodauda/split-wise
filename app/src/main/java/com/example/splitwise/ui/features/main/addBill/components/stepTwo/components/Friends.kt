package com.example.splitwise.ui.features.main.addBill.components.stepTwo.components

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun Friends(
    modifier: Modifier = Modifier
) {
    Column(
        modifier
    ) {
        Text(
            text = stringResource(R.string.select_friends),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            contentPadding = PaddingValues(bottom = Spacing.extraMedium)
        ) {
            items(10) {
                Friend()
            }
        }
    }
}

@Composable
fun Friend(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .border(
                width = ComponentDimensions.borderWidthMedium, shape = MaterialTheme.shapes.large,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(Spacing.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(ComponentDimensions.iconSizeExtraLarge)
                    .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
            ) {
                Text(
                    text = "M",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column{
                Text(
                    text = "Mike Chan",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "example@example.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(ComponentDimensions.iconSizeMedium)
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                .padding(6.dp)
        ) {
            Icon(
                painter = painterResource(R.drawable.check_icon),
                contentDescription = "check icon",
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FriendsPreview() {
    SplitWiseTheme {
        Friends()
    }
}