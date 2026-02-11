package com.example.splitwise.ui.features.main.friends.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppIconTextButton
import com.example.splitwise.ui.theme.BalanceNegative
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.extendedColorScheme

@Composable
fun EmptyFriendView(
    onInviteFriend: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
            .padding(top = 80.dp, start = 60.dp, end = 60.dp)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .background(color = MaterialTheme.extendedColorScheme.disabledContainer, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.add_friend_icon),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface
            )
        }
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = stringResource(R.string.no_friends),
            style = BalanceNegative,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.small))
        Text(
            text = stringResource(R.string.no_friends_desc),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(Spacing.large))
        AppIconTextButton(
            leadingIcon = R.drawable.add_friend_icon,
            title = stringResource(R.string.invite_first_friend),
            onClick = {onInviteFriend()}
        )
    }
}