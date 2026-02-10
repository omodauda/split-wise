package com.example.splitwise.ui.features.main.addBill.components.stepTwo.components

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.model.Group
import com.example.splitwise.ui.theme.ComponentDimensions
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.emerald_50

@Composable
fun Groups(
    modifier: Modifier = Modifier
) {
    var selectedGroup by rememberSaveable { mutableStateOf<String?>(null) }
    val groups = listOf(
        Group(
            id = "1",
            name = "Weekend Trip",
            memberCount = 3
        ),
        Group(
            id = "2",
            name = "Tennis",
            memberCount = 15
        ),
        Group(
            id = "3",
            name = "South Club",
            memberCount = 2
        )
    )
    Column(
        modifier
    ) {
        Text(
            text = stringResource(R.string.select_a_group),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            contentPadding = PaddingValues(bottom = Spacing.extraMedium)
        ) {
            items(groups) { group ->
                val isSelected = group.id == selectedGroup
                Group(
                    group,
                    isSelected,
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClick = {selectedGroup = group.id}
                        )
                )
            }
        }
    }
}

@Composable
fun Group(
    group: Group,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = modifier
            .fillMaxWidth()
            .background(color = if (isSelected) emerald_50 else MaterialTheme.colorScheme.background)
            .border(
                width = ComponentDimensions.borderWidthMedium, shape = MaterialTheme.shapes.large,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant
            )
            .padding(Spacing.medium)
    ) {
        Box(
            modifier = Modifier
                .size(ComponentDimensions.iconSizeExtraLarge)
                .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
        )
        Column{
            Text(
                text = group.name,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = pluralStringResource(
                    R.plurals.member_count,
                    group.memberCount,
                    group.memberCount
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun GroupsPreview() {
    SplitWiseTheme {
        Groups()
    }
}