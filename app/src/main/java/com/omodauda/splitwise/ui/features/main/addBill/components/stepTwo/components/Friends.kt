package com.omodauda.splitwise.ui.features.main.addBill.components.stepTwo.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.paging.PagingData
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.Friend
import com.omodauda.splitwise.ui.components.UserAvatar
import com.omodauda.splitwise.ui.theme.ComponentDimensions
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseTheme
import kotlinx.coroutines.flow.flowOf

@Composable
fun Friends(
    friends: LazyPagingItems<Friend>,
    selectedFriends: List<String>,
    currentUserId: String?,
    onSelectFriend: (user: Friend) -> Unit,
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
        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 300.dp),
            verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            contentPadding = PaddingValues(bottom = Spacing.extraMedium)
        ) {
            items(friends.itemCount) {index ->
                val friend = friends[index]
                if (friend !== null) {
                    val isSelected = friend.userId in selectedFriends
                    val isMe = friend.userId == currentUserId
                    Friend(
                        user = friend,
                        isSelected = isSelected,
                        isMe = isMe,
                        modifier = Modifier
                            .clickable {
                                onSelectFriend(friend)
                            }
                    )
                }
            }
        }
    }
}

@Composable
fun Friend(
    user: Friend,
    isSelected: Boolean,
    isMe: Boolean,
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
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(Spacing.medium)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
//            Box(
//                contentAlignment = Alignment.Center,
//                modifier = Modifier
//                    .size(ComponentDimensions.iconSizeExtraLarge)
//                    .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
//            ) {
//                Text(
//                    text = "M",
//                    style = MaterialTheme.typography.titleMedium,
//                    color = MaterialTheme.colorScheme.onSurfaceVariant
//                )
//            }
            UserAvatar(fullName = user.fullName, avatarUrl = user.avatar)
            Column{
                Text(
                    text = if (isMe) "You" else user.fullName,
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
        if (isSelected) {
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
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FriendsPreview() {
    val emptyFriends = flowOf(PagingData.empty<Friend>()).collectAsLazyPagingItems()
    SplitWiseTheme {
        Friends(onSelectFriend = {}, selectedFriends = emptyList(), friends = emptyFriends, currentUserId = "")
    }
}