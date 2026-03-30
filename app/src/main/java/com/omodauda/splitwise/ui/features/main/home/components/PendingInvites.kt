package com.omodauda.splitwise.ui.features.main.home.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.FriendInvite
import com.omodauda.splitwise.ui.components.AppIconTextButton
import com.omodauda.splitwise.ui.components.AppTextButton
import com.omodauda.splitwise.ui.components.UserAvatar
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.SplitWiseTheme
import com.omodauda.splitwise.ui.theme.emerald_500
import com.omodauda.splitwise.utils.formatRelativeTime
import kotlinx.coroutines.flow.flowOf

@Composable
fun PendingInvites(
    dismiss: () -> Unit,
    invites: LazyPagingItems<FriendInvite>,
    onAccept: (String) -> Unit,
    onDecline: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Scaffold(
            topBar = { PendingInvitesHeader(count = invites.itemCount) },
            bottomBar = { PendingInviteFooter( onDismiss = {dismiss()}) },
            modifier = modifier
                .fillMaxSize()
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(top = innerPadding.calculateTopPadding())
            ) {

                LazyColumn(
                    contentPadding = PaddingValues(
                        start = Spacing.medium,
                        end = Spacing.medium,
                        top = Spacing.large,
                        bottom = innerPadding.calculateBottomPadding() + Spacing.medium
                    ),
                    verticalArrangement = Arrangement.spacedBy(Spacing.medium)
                ) {
                    items(
                        count = invites.itemCount,
                        key = { index -> invites[index]?.id ?: index }
                    ) { index ->
                        val invite = invites[index]
                        if (invite != null) {
                            PendingInviteCard(
                                invite = invite,
                                onAccept = onAccept,
                                onDecline = onDecline
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun PendingInvitesHeader(
    count: Int,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.primary)
            .systemBarsPadding()
            .padding(bottom = ScreenDimensions.itemSpacing, top = 47.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(64.dp)
                .clip(shape = MaterialTheme.shapes.large)
                .background(color = emerald_500)
        ) {
            Icon(
                painter = painterResource(R.drawable.add_friend_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier
                    .size(32.dp)
            )
        }
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = stringResource(R.string.pending_invites),
            style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.height(Spacing.small))
        Text(
            text = pluralStringResource(id = R.plurals.invite_count, count = count, count),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun PendingInviteCard(
    invite: FriendInvite,
    onAccept: (id: String) -> Unit,
    onDecline: (id: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = SplitWiseShapes.card)
            .background(color = MaterialTheme.colorScheme.background, shape = SplitWiseShapes.card)
            .padding(top = Spacing.large, start = Spacing.large, end = Spacing.large, bottom = Spacing.medium)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            UserAvatar(
                fullName = invite.sender.fullName,
                avatarUrl = invite.sender.avatar
            )
            Column {
                Text(
                    text = invite.sender.fullName,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold),
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(Spacing.extraSmall))
                Text(
                    text = invite.sender.email,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(Spacing.extraSmall))
                Text(
                    text = formatRelativeTime(invite.createdAt),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(Modifier.height(Spacing.medium))
        Row(
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppIconTextButton(
                leadingIcon = R.drawable.check_icon,
                title = stringResource(R.string.accept),
                onClick = {onAccept(invite.id)},
                modifier = Modifier
                    .weight(1f),
            )
            AppIconTextButton(
                leadingIcon = R.drawable.close_icon,
                title = stringResource(R.string.decline),
                onClick = {onDecline(invite.id)},
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = Color.Black,
                modifier = Modifier
                    .weight(1f),
            )
        }
    }
}

@Composable
fun PendingInviteFooter(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 2.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(ScreenDimensions.contentPadding)
    ) {
        AppTextButton(
            title = stringResource(R.string.decide_later),
            onClick = {onDismiss()},
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
            contentColor = Color.Black,
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun PendingInvitesPreview() {
    val emptyInvites = flowOf(PagingData.empty<FriendInvite>()).collectAsLazyPagingItems()
    SplitWiseTheme {
        PendingInvites(
            dismiss = {}, invites = emptyInvites,
            onAccept = {},
            onDecline = {}
        )
    }
}
