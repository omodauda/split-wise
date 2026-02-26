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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.example.splitwise.R
import com.example.splitwise.data.network.model.Friend
import com.example.splitwise.mock.FakeAppContainer
import com.example.splitwise.model.InviteSubmissionState
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.features.main.friends.components.EmptyFriendView
import com.example.splitwise.ui.features.main.friends.components.EmptySearchView
import com.example.splitwise.ui.features.main.friends.components.InviteFriendModal
import com.example.splitwise.ui.features.main.invites.InviteViewModel
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.valentinilk.shimmer.shimmer

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendScreen(
    viewModel: FriendViewModel,
    inviteViewModel: InviteViewModel,
    modifier: Modifier = Modifier
) {
    val inviteUiState by inviteViewModel.uiState.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showInviteModal by remember { mutableStateOf(false) }

    val friends = viewModel.friendsPagingData.collectAsLazyPagingItems()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    val context = LocalContext.current
    fun shareInvite () {
        val inviteLink = "https://splitwise-app.example.com/invite/234509"
        val shareMessage = "Join me on SplitWise to easily split our bills! Click the link to join: $inviteLink"
        val shareTitle = "Join me on SplitWise!"

        val sendIntent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, shareMessage)
            putExtra(Intent.EXTRA_SUBJECT, shareTitle)
            type = "text/plain"
        }

        val shareIntent = Intent.createChooser(sendIntent, null)
        context.startActivity(shareIntent)
    }

    fun openInviteModal() {
        showInviteModal = true
    }

    Scaffold(
        topBar = {
            FriendHeader(
                onSendInvite = {openInviteModal()},
                searchQuery = searchQuery,
                onSearchChanged = {
                    viewModel.onSearchQueryChanged(it)}
            )},
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            FriendsList(
                friends = friends, sendInvite = { openInviteModal() }, searchQuery = searchQuery,
                onRefresh = { friends.refresh() },
            )

            if (showInviteModal) {
                InviteFriendModal(
                    sheetState = sheetState,
                    onDismissRequest = { showInviteModal = false },
                    shareInvite = { shareInvite() },
                    validateForm = { inviteViewModel.validateEmail() },
                    email = inviteUiState.email,
                    onEmailChanged = { inviteViewModel.onEmailChanged(it) },
                    isLoading = inviteUiState.sendInviteState is InviteSubmissionState.Loading,
                    emailError = inviteUiState.emailError,
                    sendEmailInvite = {inviteViewModel.sendInvite(it)},
                    sendInviteState = inviteUiState.sendInviteState,
                    resetSendInviteState = {inviteViewModel.resetSendInviteState()}
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FriendsList(
    friends: LazyPagingItems<Friend>,
    searchQuery: String?,
    sendInvite: () -> Unit,
    onRefresh: () -> Unit,
    modifier: Modifier = Modifier
) {
    val loadState = friends.loadState
    val isRefreshing = friends.loadState.refresh is LoadState.Loading

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        if (loadState.refresh is LoadState.Loading) {
            // handle refresh loading
            FriendListPlaceholder()
        } else if (!searchQuery.isNullOrBlank() && friends.itemCount == 0) {
            EmptySearchView()
        } else if (friends.itemCount == 0) {
            EmptyFriendView(onInviteFriend = {sendInvite()})
        } else {
            PullToRefreshBox(
                isRefreshing = isRefreshing,
                onRefresh = { onRefresh()},
                modifier = modifier.fillMaxSize()
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Spacing.large),
                    contentPadding = PaddingValues(
                        start = Spacing.large,
                        end = Spacing.large,
                        bottom = Spacing.large,
                        top = Spacing.large
                    ),
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                ) {
                    stickyHeader {
                        Text(
                            text = pluralStringResource(
                                R.plurals.friend_count,
                                friends.itemCount,
                                friends.itemCount
                            ),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    items(
                        count = friends.itemCount,
                        key = friends.itemKey { it.friendshipId }) { index ->
                        val friend = friends[index]
                        if (friend !== null) {
                            FriendView(user = friend)
                            HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceVariant
                            )
                        } else {
                            // TODO: show skeleton placeholder
                            FriendPlaceholder()
                        }
                    }

                    // Auto show loading states at the bottom of list
                    item {
                        when (loadState.append) {
                            is LoadState.Loading -> {
                                Box(
                                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }

                            is LoadState.Error -> {

                            }

                            else -> {}
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun FriendView(
    user: Friend,
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
                text = user.fullName,
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
    searchQuery: String?,
    onSearchChanged: (String?) -> Unit,
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
            value = searchQuery ?: "",
            onValueChange = {onSearchChanged(it)},
            placeholder = stringResource(R.string.search_friends),
            leadingIcon = R.drawable.search_icon
        )
    }
}

@Composable
fun FriendListPlaceholder(
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .shimmer(),
        contentPadding = PaddingValues(
            start = Spacing.large,
            end = Spacing.large,
            bottom = Spacing.large,
            top = Spacing.large
        ),
    ) {
        items(12) {
            FriendPlaceholder()
            HorizontalDivider(
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
}

@Composable
fun FriendPlaceholder(
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium)
            .shimmer()
    ) {
        Box(
            modifier = Modifier
                .size(ComponentDimensions.iconSizeExtraLarge)
                .background(color = Color.LightGray, shape = CircleShape)
        )
        Column{
            Box(
                modifier = Modifier
                    .height(20.dp)
                    .fillMaxWidth(0.7f)
                    .background(
                        color = Color.LightGray,
                        shape = MaterialTheme.shapes.small
                    )
            )
            Spacer(Modifier.height(2.dp))
            Box(
                modifier = Modifier
                    .height(16.dp)
                    .fillMaxWidth(0.4f)
                    .background(
                        color = Color.LightGray,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun FriendScreenPreview() {
    val container = FakeAppContainer()
    val vm = FriendViewModel(container.friendRepository)
    val inviteVm = InviteViewModel(container.inviteRepository)
//    val toastHostState = rememberToastHostState()
    SplitWiseTheme {
        FriendScreen(viewModel = vm, inviteViewModel = inviteVm)
    }
}