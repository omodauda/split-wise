package com.example.splitwise.ui.features.main.accountSettings

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.splitwise.R
import com.example.splitwise.mock.FakeAppContainer
import com.example.splitwise.model.SubmissionState
import com.example.splitwise.ui.components.AppTextButton
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.features.auth.AuthViewModel
import com.example.splitwise.ui.features.main.accountSettings.components.ChangePasswordDialog
import com.example.splitwise.ui.features.main.accountSettings.components.ChangePasswordModal
import com.example.splitwise.ui.features.main.accountSettings.components.DeleteAccountDialog
import com.example.splitwise.ui.features.main.accountSettings.components.DeleteAccountModal
import com.example.splitwise.ui.theme.Elevation
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.emerald_50
import com.example.splitwise.ui.theme.error_light

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountSettingScreen(
    goBack: () -> Unit,
    changePasswordViewModel: ChangePasswordViewModel,
    deleteAccountViewModel: DeleteAccountViewModel,
    authViewModel: AuthViewModel,
    modifier: Modifier = Modifier
) {
    val uiState by changePasswordViewModel.uiState.collectAsState()
    val deleteAccountUiState by deleteAccountViewModel.uiState.collectAsState()
    val user by authViewModel.user.collectAsStateWithLifecycle()
    var editableFullName by remember { mutableStateOf("${user?.fullName}") }

    // change password
    val changePasswordModalState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showChangePasswordModal by remember { mutableStateOf(false) }
    // delete account
    val deleteAccountModalState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showDeleteAccountModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
                .verticalScroll(rememberScrollState())
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
            ) {
                AccountSettingHeader(goBack, paddingTop = innerPadding.calculateTopPadding())
                HorizontalDivider(thickness = 7.dp)
                PersonalInfoSection(
                    email = user?.email,
                    fullName = editableFullName,
                    onFullNameChange = { editableFullName = it }
                )
                HorizontalDivider(thickness = 7.dp)
                SecuritySection(openChangePasswordModal = { showChangePasswordModal = true })
                HorizontalDivider(thickness = 7.dp)
                DangerZone(onDeleteAccount = { showDeleteAccountModal = true })
            }
            Column(
                modifier = modifier
                    .shadow(elevation = Elevation.level5)
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(
                        top = ScreenDimensions.verticalPadding,
                        bottom = ScreenDimensions.verticalPadding + innerPadding.calculateBottomPadding(),
                        start = Spacing.large,
                        end = Spacing.large
                    )

            ) {
                AppTextButton(
                    title = stringResource(R.string.save_changes),
                    onClick = {},
                )
            }
            if (showChangePasswordModal) {
                ChangePasswordModal(
                    sheetState = changePasswordModalState,
                    onDismissRequest = { showChangePasswordModal = false },
                    uiState = uiState,
                    onCurrentPasswordChange = { changePasswordViewModel.onCurrentPasswordChange(it) },
                    onNewPasswordChange = { changePasswordViewModel.onNewPasswordChanged(it) },
                    onConfirmNewPasswordChange = {
                        changePasswordViewModel.onConfirmNewPasswordChange(
                            it
                        )
                    },
                    onChangePassword = { changePasswordViewModel.changePassword() }
                )
            }
            if (showDeleteAccountModal) {
                DeleteAccountModal(
                    sheetState = deleteAccountModalState,
                    onDismissRequest = { showDeleteAccountModal = false },
                    viewModel = deleteAccountViewModel
                )
            }
            if (uiState.submissionState != SubmissionState.Idle) {
                ChangePasswordDialog(
                    uiState = uiState
                )
            }
            if (deleteAccountUiState.submissionState != SubmissionState.Idle) {
                DeleteAccountDialog(uiState = deleteAccountUiState)
            }
        }
    }
}


@Composable
fun AccountSettingHeader(
    goBack: () -> Unit,
    paddingTop: Dp,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = Spacing.small,
                end = Spacing.small,
                top = paddingTop + Spacing.extraMedium,
                bottom = Spacing.extraMedium
            )
    ) {
        IconButton(
            onClick = { goBack() }
        ) {
            Icon(
                painter = painterResource(R.drawable.caret_left),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground
            )
        }
        Text(
            text = stringResource(R.string.account_settings),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun PersonalInfoSection(
    fullName: String?,
    onFullNameChange: (String) -> Unit,
    email: String?,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxWidth()

            .background(color = MaterialTheme.colorScheme.background)
            .padding(ScreenDimensions.sectionSpacing)
    ) {
        Text(
            text = stringResource(R.string.personal_info).uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(Spacing.medium))
        AppTextField(
            value = fullName ?: "",
            onValueChange = onFullNameChange,
            label = stringResource(R.string.full_name)
        )
        AppTextField(
            value = email ?: "",
            onValueChange = {},
            label = stringResource(R.string.email),
            enabled = false
        )
    }
}

@Composable
fun SecuritySection(
    openChangePasswordModal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()

            .background(color = MaterialTheme.colorScheme.background)
            .padding(ScreenDimensions.sectionSpacing)
    ) {
        Text(
            text = stringResource(R.string.security).uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(Spacing.medium))
        SettingsItem(
            title = R.string.change_password,
            subTitle = R.string.change_password_subtitle,
            icon = R.drawable.password_icon,
            modifier = Modifier
                .clickable(
                    enabled = true,
                    onClick = { openChangePasswordModal() }
                )
        )
    }
}

@Composable
fun DangerZone(
    onDeleteAccount: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(ScreenDimensions.sectionSpacing)
    ) {
        Text(
            text = stringResource(R.string.danger_zone).uppercase(),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(Spacing.medium))
        SettingsItem(
            title = R.string.delete_account,
            subTitle = R.string.delete_account_subtitle,
            icon = R.drawable.bin_icon,
            color = error_light,
            modifier = Modifier
                .clickable(enabled = true, onClick = { onDeleteAccount() })
        )
    }
}

@Composable
fun SettingsItem(
    title: Int,
    icon: Int,
    subTitle: Int,
    modifier: Modifier = Modifier,
    color: Color? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .padding(
                top = ScreenDimensions.itemSpacing,
                bottom = ScreenDimensions.itemSpacing,
                end = Spacing.small
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(
                        color = if (color !== null) MaterialTheme.colorScheme.errorContainer else emerald_50,
                        shape = CircleShape
                    )
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint = color ?: MaterialTheme.colorScheme.primary
                )
            }
            Column {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Spacer(Modifier.height(1.dp))
                Text(
                    text = stringResource(subTitle),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Icon(
            painter = painterResource(R.drawable.forward_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun AccountSettingScreenPreview() {
    val vm = ChangePasswordViewModel()
    val container = FakeAppContainer()
    val deleteVm = DeleteAccountViewModel(container.authRepository)
    val authViewModel = AuthViewModel(container.authRepository)
    SplitWiseTheme {
        AccountSettingScreen(
            goBack = {},
            changePasswordViewModel = vm,
            deleteAccountViewModel = deleteVm,
            authViewModel
        )
    }
}