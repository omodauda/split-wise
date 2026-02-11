package com.example.splitwise.ui.features.main.activity

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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.model.ActivityItem
import com.example.splitwise.model.ActivitySection
import com.example.splitwise.ui.features.main.activity.components.EmptyActivity
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import java.util.Locale

@Composable
fun ActivityScreen(
    modifier: Modifier = Modifier
) {
    val activities = listOf(
        ActivitySection(
            title = "FEBRUARY 2026",
            items = listOf(
                ActivityItem(id = "1", title = "Settled up", amount = 150.00, payer = "james Wilson", paid = "You" ),
                ActivityItem(id = "2", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "3", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "4", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "5", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "6", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "7", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
            )
        ),
        ActivitySection(
            title = "JANUARY 2026",
            items = listOf(
                ActivityItem(id = "8", title = "Settled up", amount = 150.00, payer = "james Wilson", paid = "You" ),
                ActivityItem(id = "9", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "10", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "11", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "12", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "13", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
                ActivityItem(id = "14", title = "Settled up", amount = 350.00, payer = "You", paid = "Sarah Johnson" ),
            )
        )
    )

//    val activities = listOf<ActivitySection>()

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        topBar = {ActivityHeader()}
    ) { innerPadding ->
        if (activities.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(innerPadding)
            ) {
                EmptyActivity()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.inverseOnSurface)
                    .padding(top = innerPadding.calculateTopPadding(), start = innerPadding.calculateLeftPadding(layoutDirection = LayoutDirection.Ltr), end = innerPadding.calculateRightPadding(layoutDirection = LayoutDirection.Ltr))
            ) {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(Spacing.large),
                    contentPadding = PaddingValues(start = Spacing.large, end = Spacing.large, bottom = Spacing.large, top = Spacing.large),
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    activities.forEach { section ->
                        stickyHeader {
                            ActivitySectionHeader(title = section.title)
                        }

                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .shadow(elevation = 1.dp, shape = SplitWiseShapes.card)
                                    .background(color = MaterialTheme.colorScheme.background, shape = SplitWiseShapes.card)

                            ) {
                                section.items.forEachIndexed { index, activityItem ->
                                    ActivityItemView(item = activityItem)
                                    if (index < section.items.lastIndex) {
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

@Composable
fun ActivitySectionHeader(
    title: String,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Icon(
            painter = painterResource(R.drawable.calendar_icon),
            contentDescription = "calendar icon"
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun ActivityItemView(
    item: ActivityItem,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.medium)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
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
                    text = "${item.payer} → ${item.paid}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(Modifier.height(Spacing.extraSmall))
                Text(
                    text = "Roommates",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "$${String.format(locale = Locale.US, format = "%.2f", item.amount)}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(Modifier.height(Spacing.extraSmall))
            Text(
                text = "Jan 12",
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
        ActivityScreen()
    }
}