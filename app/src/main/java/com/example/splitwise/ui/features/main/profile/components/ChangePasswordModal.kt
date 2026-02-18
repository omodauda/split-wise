package com.example.splitwise.ui.features.main.profile.components

import androidx.compose.animation.core.copy
import androidx.compose.foundation.background
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.splitwise.R
import com.example.splitwise.model.ChangePasswordUiState
import com.example.splitwise.model.SubmissionState
import com.example.splitwise.ui.components.AppTextButton
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.features.main.profile.ChangePasswordViewModel
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.emerald_50

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChangePasswordModal(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    uiState: ChangePasswordUiState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    onChangePassword: () -> Unit
//    viewModel: ChangePasswordViewModel,
) {
//    val uiState by viewModel.uiState.collectAsState()

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
        ) {
            ChangePasswordModalHeader(onDismiss = {onDismissRequest()}, title = R.string.change_password)
            ChangePasswordModalContent(
                uiState = uiState,
                onCurrentPasswordChange = {onCurrentPasswordChange(it)},
                onNewPasswordChange = {onNewPasswordChange(it)},
                onConfirmNewPasswordChange = {onConfirmNewPasswordChange(it)}
            )
            ChangePasswordModalFooter(
                isValid = uiState.isFormValid,
                onChangePassword = {
                    onDismissRequest()
                   onChangePassword()
                }
            )
        }
    }
}

@Composable
fun ChangePasswordModalHeader(
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
                    contentDescription = null
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
fun ChangePasswordModalContent(
    uiState: ChangePasswordUiState,
    onCurrentPasswordChange: (String) -> Unit,
    onNewPasswordChange: (String) -> Unit,
    onConfirmNewPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(start = Spacing.large, end = Spacing.large, top = Spacing.large)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = stringResource(R.string.change_password_desc),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(Modifier.height(Spacing.extraSmall))
        AppTextField(
            value = uiState.currentPassword,
            onValueChange = {onCurrentPasswordChange(it)},
            isPassword = true,
            label = stringResource(R.string.current_password),
            placeholder = stringResource(R.string.current_password_placeholder),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
        )
        AppTextField(
            value = uiState.newPassword,
            onValueChange = {onNewPasswordChange(it)},
            isPassword = true,
            label = stringResource(R.string.new_password),
            placeholder = stringResource(R.string.new_password_placeholder),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
        )
        if (uiState.newPassword.isNotEmpty()) {
            ChangePasswordRequirements(
                hasMinCars = uiState.hasMinChars,
                hasUppercase = uiState.hasUppercase,
                hasLowercase = uiState.hasLowercase,
                hasNumber = uiState.hasNumber,
                hasSpecialChar = uiState.hasSpecialChar
            )
        }
        Spacer(Modifier.height(Spacing.extraMedium))
        AppTextField(
            value = uiState.confirmNewPassword,
            onValueChange = {onConfirmNewPasswordChange(it)},
            isPassword = true,
            label = stringResource(R.string.confirm_new_password),
            placeholder = stringResource(R.string.confirm_new_password_placeholder),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Done
            ),
            isError = uiState.confirmNewPassword.isNotEmpty() && !uiState.newPasswordMatch,
            errorMessage = stringResource(R.string.password_match_error)
        )
    }
}

@Composable
fun ChangePasswordRequirements(
    hasMinCars: Boolean,
    hasUppercase: Boolean,
    hasLowercase: Boolean,
    hasNumber: Boolean,
    hasSpecialChar: Boolean,
    modifier: Modifier = Modifier
) {
    val requirements = stringArrayResource(R.array.change_password_guide_items)
    val requirementStates = mapOf(
        requirements[0] to hasMinCars,
        requirements[1] to hasUppercase,
        requirements[2] to hasLowercase,
        requirements[3] to hasNumber,
        requirements[4] to hasSpecialChar
    )
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.inverseOnSurface,
                shape = SplitWiseShapes.card
            )
            .padding(Spacing.medium)
    ) {
        Text(
            text = stringResource(R.string.password_requirements),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        Column(
            verticalArrangement = Arrangement.spacedBy(Spacing.small),
        ) {
            requirementStates.forEach { (title, isSatisfied) ->
                ChangeRequirementItem(title = title, isSatisfied)
            }
        }
    }
}

@Composable
fun ChangeRequirementItem(
    title: String,
    isSatisfied: Boolean,
    modifier: Modifier = Modifier
) {
    val iconBackgroundColor = if (isSatisfied) emerald_50 else MaterialTheme.colorScheme.surfaceVariant
    val iconTintColor = if (isSatisfied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
    val textColor = if (isSatisfied) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
        modifier = modifier
        .fillMaxWidth()
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(20.dp)
                .background(color = iconBackgroundColor, shape = CircleShape)
        ) {
            Icon(
                painter = painterResource(R.drawable.check_icon),
                contentDescription = null,
                tint = iconTintColor,
                modifier = Modifier
                    .size(8.dp)
            )
        }
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = textColor
        )
    }
}

@Composable
fun ChangePasswordModalFooter(
    isValid: Boolean,
    onChangePassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .shadow(1.dp)
            .padding(horizontal = Spacing.large, vertical = Spacing.medium)
    ) {
        AppTextButton(
            title = stringResource(R.string.cancel),
            onClick = {},
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.onBackground,
            modifier = Modifier
                .weight(1f)
        )
        AppTextButton(
            title = stringResource(R.string.change_password),
            enabled = isValid,
            onClick = {onChangePassword()},
            modifier = Modifier
                .weight(1f)
        )
    }
}

