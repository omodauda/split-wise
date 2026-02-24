package com.example.splitwise.ui.features.main.friends.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.emerald_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendModal(
    sheetState: SheetState,
    shareInvite: () -> Unit,
    onDismissRequest: () -> Unit,
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
            InviteFriendModalHeader(onDismiss = {onDismissRequest()}, title = R.string.invite_friends)
            InviteFriendModalContent(
                shareInvite = {
                    shareInvite()
                }
            )
        }
    }
}

@Composable
fun InviteFriendModalHeader(
    title: Int,
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
        Row(
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(color = emerald_50, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.password_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = stringResource(title),
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
    HorizontalDivider(thickness = 0.4.dp, color = MaterialTheme.colorScheme.onSurface)
}

@Composable
fun InviteFriendModalContent(
    shareInvite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(Spacing.large)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.small),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppTextField(
                label = stringResource(R.string.invite_via_email),
                placeholder = stringResource(R.string.invite_placeholder),
                value = "",
                onValueChange = {},
                modifier = Modifier.weight(0.7f)
            )
            TextButton(
                onClick = {},
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(horizontal = Spacing.large, vertical = ScreenDimensions.itemSpacing),
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = MaterialTheme.colorScheme.onSurface,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                Text(
                    text = stringResource(R.string.send),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(Modifier.height(Spacing.large))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            modifier = modifier
                .fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = stringResource(R.string.or_via),
                color = MaterialTheme.colorScheme.onBackground
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
            )
        }
        Spacer(Modifier.height(Spacing.large))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(color = Color.LightGray, shape = SplitWiseShapes.card)
                    .padding(vertical = 16.dp, horizontal = 64.dp)
                    .clickable(enabled = true, onClick = {shareInvite()})
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = emerald_50, shape = CircleShape)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.share_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}