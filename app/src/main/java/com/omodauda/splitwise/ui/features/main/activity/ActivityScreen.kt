package com.omodauda.splitwise.ui.features.main.activity

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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.Activity
import com.omodauda.splitwise.ui.features.main.activity.components.EmptyActivity
import com.omodauda.splitwise.ui.features.main.friends.FriendListPlaceholder
import com.omodauda.splitwise.ui.features.main.friends.FriendPlaceholder
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseTheme
import com.omodauda.splitwise.utils.formatDate
import com.omodauda.splitwise.utils.formatFromCents

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityScreen(
    viewModel: ActivityViewModel,
    modifier: Modifier = Modifier
) {
    val activities = viewModel.activitiesPagingData.collectAsLazyPagingItems()
    val loadState = activities.loadState
    val isRefreshing = activities.loadState.refresh is LoadState.Loading


    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {ActivityHeader()}
    ) { innerPadding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            if (loadState.refresh is LoadState.Loading) {
                // handle refresh loading
                FriendListPlaceholder()
            }  else if (activities.itemCount == 0) {
                EmptyActivity()
            } else {
                PullToRefreshBox(
                    isRefreshing = isRefreshing,
                    onRefresh = {activities.refresh()},
                    modifier = modifier.fillMaxSize()
                ){
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(Spacing.large),
                        contentPadding = PaddingValues(bottom = Spacing.large, top = Spacing.large),
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        items(
                            count = activities.itemCount,
                            key = activities.itemKey { it.id }) { index ->
                            val activity = activities[index]
                            if (activity !== null) {
                                ActivityItemView(item = activity)
                                HorizontalDivider(
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                )
                            } else {
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
}

@Composable
fun ActivityHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .shadow(elevation = 1.dp)
            .padding(ScreenDimensions.sectionSpacing)
    ) {
        Text(
            text = stringResource(R.string.activity),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.extraSmall))
        Text(
            text = stringResource(R.string.activity_sub_title),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

//@Composable
//fun ActivitySectionHeader(
//    title: String,
//    modifier: Modifier = Modifier
//) {
//    Row(
//        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
//        modifier = modifier
//            .fillMaxWidth()
//    ) {
//        Icon(
//            painter = painterResource(R.drawable.calendar_icon),
//            contentDescription = "calendar icon"
//        )
//        Text(
//            text = title,
//            style = MaterialTheme.typography.bodyMedium,
//            color = MaterialTheme.colorScheme.onSurface
//        )
//    }
//}

@Composable
fun ActivityItemView(
    item: Activity,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            modifier = Modifier
                .weight(1f)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.activity_icon),
                    contentDescription = null,
//                    tint = spectrumBlue
                )
            }
            Column {
                Text(
                    text = item.title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = item.body,
//                    text = "${item.actionedBy.fullName} → ${if (item.data.amount != null) formatFromCents(item.data.amount) else ""}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
//                Spacer(Modifier.height(Spacing.extraSmall))
//                Text(
//                    text = "Roommates",
//                    style = MaterialTheme.typography.bodySmall,
//                    color = MaterialTheme.colorScheme.onSurface
//                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = if (item.data.amount != null) formatFromCents(item.data.amount) else "",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(Spacing.extraSmall))
            Text(
                text = formatDate(item.createdAt),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ActivityScreenPreview() {
    SplitWiseTheme {
//        ActivityScreen(viewModel = vm)
    }
}