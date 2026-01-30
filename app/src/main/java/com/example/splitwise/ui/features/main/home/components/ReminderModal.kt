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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppIconTextButton
import com.example.splitwise.ui.theme.BalanceNegative
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.emerald_200

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderModal(
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
            ModalHeader(onDismiss = {onDismissRequest()})
            ModalContent()
        }
    }
}

@Composable
fun ModalHeader(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = Spacing.large, end = Spacing.large, bottom = Spacing.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(ComponentDimensions.avatarSizeMedium)
                    .background(color = MaterialTheme.colorScheme.errorContainer, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.clock_icon),
                    contentDescription = "clock icon",
                    tint = MaterialTheme.colorScheme.error
                )
            }
            Text(
                text = "Send Reminder",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(
            onClick = {onDismiss()}
        ) {
            Icon(
                painter = painterResource(R.drawable.close_icon),
                contentDescription = "close icon"
            )
        }
    }
    HorizontalDivider(thickness = 0.5.dp, color = MaterialTheme.colorScheme.onSurface)
}

@Composable
fun ModalContent(
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .padding(Spacing.large)
    ) {
        AvatarView(avatarText = "S")
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = "Sarah Johnson owes you",
            style = BalanceNegative,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        AmountView(balance = "$138.63")
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = "Send a friendly reminder to Sarah?",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Spacing.small))
        AppIconTextButton(
            onClick = {},
            leadingIcon = R.drawable.send_icon,
            title = "Send Reminder",
            modifier = Modifier
                .height(60.dp)
        )
        Spacer(Modifier.height(Spacing.large))
    }
}

@Composable
fun AvatarView(
    avatarText: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(ComponentDimensions.avatarSizeLarge)
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
    ) {
        Text(
            text = avatarText,
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun AmountView(
    balance: String,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(color = emerald_200, shape = MaterialTheme.shapes.large)
            .padding(vertical = ScreenDimensions.sectionSpacing)
    ) {
        Text(
            text = "Amount Due",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(Spacing.extraSmall))
        Text(
            text = balance,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(Modifier.height(Spacing.extraSmall))
        Text(
            text = "Outstanding balance",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )

    }
}