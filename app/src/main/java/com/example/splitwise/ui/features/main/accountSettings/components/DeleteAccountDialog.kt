package com.example.splitwise.ui.features.main.accountSettings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.splitwise.R
import com.example.splitwise.model.SubmissionState
import com.example.splitwise.ui.features.main.accountSettings.DeleteAccountUiState
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes

@Composable
fun DeleteAccountDialog(
    uiState: DeleteAccountUiState
) {
    Dialog(
        onDismissRequest = { },
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            dismissOnBackPress = false,
            dismissOnClickOutside = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            DeleteAccountContentCard(
                uiState,
                modifier = Modifier.padding(horizontal = Spacing.medium)
            )
        }
    }
}

@Composable
fun DeleteAccountContentCard(
    uiState: DeleteAccountUiState,
    modifier: Modifier = Modifier
) {
    val title = if (uiState.submissionState == SubmissionState.Loading) R.string.deleting_account else R.string.account_deleted
    val subTitle = if (uiState.submissionState == SubmissionState.Loading) R.string.delete_account_subtitle else R.string.account_deleted_desc

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background, shape = SplitWiseShapes.dialog)
            .padding(Spacing.extraLarge)
    ) {

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(80.dp)
                .background(color = MaterialTheme.colorScheme.errorContainer, shape = CircleShape)
        ) {
            if (uiState.submissionState == SubmissionState.Loading) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.error)
            } else if (uiState.submissionState == SubmissionState.Success) {
                Icon(
                    painter = painterResource(R.drawable.check_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .size(26.dp)
                )
            }
        }
        Spacer(Modifier.height(Spacing.medium))
        Text(
            text = stringResource(title),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(Spacing.small))
        Text(
            text = stringResource(subTitle),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}