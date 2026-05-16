package com.omodauda.splitwise.ui.features.main.friends.components

import android.widget.Toast
import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.SendFriendInviteRequest
import com.omodauda.splitwise.model.InviteSubmissionState
import com.omodauda.splitwise.ui.components.AppTextField
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.emerald_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InviteFriendModal(
    sheetState: SheetState,
    shareInvite: () -> Unit,
    onDismissRequest: () -> Unit,
    validateForm: () -> Boolean,
    email: String,
    onEmailChanged: (String) -> Unit,
    isLoading: Boolean,
    emailError: String?,
    sendEmailInvite: (data: SendFriendInviteRequest) -> Unit,
    sendInviteState: InviteSubmissionState,
    resetSendInviteState: () -> Unit
) {
    val context = LocalContext.current

    fun handleSendInvite() {
        if (validateForm()) {
            sendEmailInvite(
                SendFriendInviteRequest(
                    receiverEmail = email
                )
            )
        }
    }

    LaunchedEffect(sendInviteState) {
        when (sendInviteState) {
            is InviteSubmissionState.Error -> {
                // Show an Android Toast on error
                Toast.makeText(context, sendInviteState.message, Toast.LENGTH_LONG).show()
                resetSendInviteState()
            }
            is InviteSubmissionState.Success -> {
                onDismissRequest()
                Toast.makeText(context, "Invite sent successfully!", Toast.LENGTH_LONG).show()
                resetSendInviteState()
            }
            else -> {
                // Do nothing for Idle or Loading states
            }
        }
    }

    ModalBottomSheet(
        onDismissRequest = {onDismissRequest()},
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = SplitWiseShapes.bottomSheet
                )
                .verticalScroll(rememberScrollState())
        ) {
            InviteFriendModalHeader(onDismiss = {onDismissRequest()}, title = R.string.invite_friends)
            InviteFriendModalContent(
                shareInvite = {
                    shareInvite()
                },
                email = email,
                onEmailChanged = onEmailChanged,
                onSendInvite = {handleSendInvite()},
                emailError = emailError,
                isLoading = isLoading
            )
        }
    }
}

@Composable
fun InviteFriendModalHeader(
    title: Int,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(start = Spacing.large, end = Spacing.large, bottom = Spacing.extraSmall)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(color = emerald_50, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.password_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Text(
                text = stringResource(title),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        IconButton(
            onClick = {onDismiss()}
        ) {
            Icon(
                painter = painterResource(R.drawable.close_icon),
                contentDescription = "close icon"
            )
        }
    }
    HorizontalDivider(thickness = 0.4.dp, color = MaterialTheme.colorScheme.onSurface)
}

@Composable
fun InviteFriendModalContent(
    email: String,
    emailError: String?,
    onEmailChanged: (String) -> Unit,
    shareInvite: () -> Unit,
    onSendInvite: () -> Unit,
    isLoading: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(Spacing.large)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(Spacing.small),
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            AppTextField(
                label = stringResource(R.string.invite_via_email),
                placeholder = stringResource(R.string.invite_placeholder),
                value = email,
                onValueChange = {onEmailChanged(it)},
                errorMessage = emailError,
                isError = emailError !== null,
                modifier = Modifier.weight(0.7f)
            )
            TextButton(
                onClick = {onSendInvite()},
                enabled = !isLoading,
                shape = MaterialTheme.shapes.large,
                contentPadding = PaddingValues(horizontal = Spacing.large, vertical = ScreenDimensions.itemSpacing),
                colors = ButtonColors(
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    containerColor = MaterialTheme.colorScheme.primary,
                    disabledContainerColor = Color.LightGray,
                    disabledContentColor = MaterialTheme.colorScheme.onSurface
                )
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), // Approx height of text
                        color = MaterialTheme.colorScheme.onPrimary,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = stringResource(R.string.send),
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }
        Spacer(Modifier.height(Spacing.large))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            modifier = modifier
                .fillMaxWidth()
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
            )
            Text(
                text = stringResource(R.string.or_via),
                color = MaterialTheme.colorScheme.onBackground
            )
            HorizontalDivider(
                modifier = Modifier
                    .weight(1f)
            )
        }
        Spacer(Modifier.height(Spacing.large))
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .background(color = Color.LightGray, shape = SplitWiseShapes.card)
                    .padding(vertical = 16.dp, horizontal = 64.dp)
                    .clickable(enabled = true, onClick = { shareInvite() })
            ) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(48.dp)
                        .background(color = emerald_50, shape = CircleShape)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.share_icon),
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}