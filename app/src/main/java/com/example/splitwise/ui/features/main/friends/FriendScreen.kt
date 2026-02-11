package com.example.splitwise.ui.features.main.friends

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.model.User
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.features.main.friends.components.EmptyFriendView
import com.example.splitwise.ui.features.main.friends.components.EmptySearchView
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun FriendScreen(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    fun sendInvite () {
        val inviteLink = "https://splitwise-app.example.com/invite/234509"
        val shareMessage = "Join me on SplitWise to easily split our bills! Click the link to join: $inviteLink"
        val shareTitle = "Join me on SplitWise!"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_INTENT, shareMessage)
            putExtra(Intent.EXTRA_SUBJECT, shareTitle)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    val friends = listOf(
        User(
            id = "1",
            name = "Sarah Johnson",
            email = "sarah@example.com"
        ),
        User(
            id = "2",
            name = "Mike Chen",
            email = "mike@example.com"
        ),
        User(
            id = "3",
            name = "Emma Wilson",
            email = "emma@example.com"
        )
    )

    Scaffold(
        topBar = {FriendHeader(onSendInvite = {sendInvite()})},
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                .padding(innerPadding)
        ) {
            FriendsList(friends)
//            EmptyFriendView(
//                onInviteFriend = {sendInvite()}
//            )
//            EmptySearchView()
        }
    }
}

@Composable
fun FriendsList(
    friends: List<User>,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.inverseOnSurface)
    ) {

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(Spacing.large),
            contentPadding = PaddingValues(start = Spacing.large, end = Spacing.large, bottom = Spacing.large, top = Spacing.large),
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            stickyHeader {
                Text(
                    text = pluralStringResource(R.plurals.friend_count, 6, 6),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
            item {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .shadow(elevation = 1.dp, shape = SplitWiseShapes.card)
                        .background(color = MaterialTheme.colorScheme.background, shape = SplitWiseShapes.card)
                ){
                    friends.forEachIndexed { index, friend ->
                        FriendView(user = friend)
                        if (index < friends.lastIndex) {
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FriendView(
    user: User,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium)
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(ComponentDimensions.iconSizeExtraLarge)
                .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
        ) {
            Text(
                text = "M",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        Column{
            Text(
                text = user.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = user.email,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun FriendHeader(
    onSendInvite: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(Spacing.large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.friends),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onBackground
            )

            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
                    .clickable(
                        enabled = true,
                        onClick = {onSendInvite()}
                    )
            ) {
                Icon(
                    painter = painterResource(R.drawable.add_friend_icon),
                    contentDescription = "add friend icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
        Spacer(Modifier.height(Spacing.medium))
        AppTextField(
            value = "",
            onValueChange = {},
            placeholder = stringResource(R.string.search_friends),
            leadingIcon = R.drawable.search_icon
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FriendScreenPreview() {
    SplitWiseTheme {
        FriendScreen()
    }
}