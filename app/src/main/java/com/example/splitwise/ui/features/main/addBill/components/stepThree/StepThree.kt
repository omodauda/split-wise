package com.example.splitwise.ui.features.main.addBill.components.stepThree

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.model.User
import com.example.splitwise.ui.features.main.addBill.components.stepTwo.components.Friend
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun StepThree(
    selectedParticipants: List<User>,
    onMemberSelected: (User) -> Unit,
    modifier: Modifier = Modifier
) {
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

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Spacing.large)
    ) {
        Text(
            text = stringResource(R.string.who_is_splitting),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.medium))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            contentPadding = PaddingValues(bottom = Spacing.extraMedium)
        ) {
            items(friends) {user ->
                val isSelected = selectedParticipants.any {it.id == user.id}
                Friend(
                    user,
                    isSelected,
                    modifier = Modifier
                        .clickable {
                            onMemberSelected(user)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun StepThreePreview() {
    SplitWiseTheme {
        StepThree(selectedParticipants = emptyList(), onMemberSelected = {})
    }
}