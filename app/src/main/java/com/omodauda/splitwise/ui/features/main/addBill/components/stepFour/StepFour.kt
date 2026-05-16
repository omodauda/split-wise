package com.omodauda.splitwise.ui.features.main.addBill.components.stepFour

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
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.Friend
import com.omodauda.splitwise.ui.features.main.addBill.components.stepTwo.components.Friend
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseTheme

@Composable
fun StepFour(
    onPayerSelected: (String) -> Unit,
    participants: List<Friend>,
    currentUserId: String?,
    modifier: Modifier = Modifier,
    payerId: String? = null,
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Spacing.large)
    ) {
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = stringResource(R.string.who_paid_bill),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.medium))
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            contentPadding = PaddingValues(bottom = Spacing.extraMedium)
        ) {
            items(participants) {user ->
                val isSelected = user.userId == payerId
                val isMe = currentUserId == user.userId
                Friend(
                    user,
                    isSelected,
                    isMe,
                    modifier = Modifier
                        .clickable {
                            onPayerSelected(user.userId)
                        }
                )
            }
        }
    }
}

@Preview
@Composable
fun StepFourPreview() {
    SplitWiseTheme {
        StepFour(
            payerId = null,
            onPayerSelected = {},
            participants = emptyList(),
            currentUserId = ""
        )
    }
}