package com.example.splitwise.ui.features.main.accountSettings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppTextButton
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.features.auth.AuthSubmissionState
import com.example.splitwise.ui.features.main.accountSettings.DeleteAccountUiState
import com.example.splitwise.ui.features.main.accountSettings.DeleteAccountViewModel
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.black
import com.example.splitwise.ui.theme.emerald_50
import com.example.splitwise.ui.theme.emerald_700
import com.example.splitwise.ui.theme.emerald_800
import com.example.splitwise.ui.theme.extendedColorScheme
import com.example.splitwise.ui.theme.white

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteAccountModal(
    sheetState: SheetState,
    onDismissRequest: () -> Unit,
    viewModel: DeleteAccountViewModel,
) {

    val uiState by viewModel.uiState.collectAsState()

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
            DeletePasswordModalHeader(onDismiss = {onDismissRequest()}, title = if (uiState.step == 1) R.string.delete_account else R.string.confirm_deletion)
            DeletePasswordModalContent(
                uiState = uiState,
                onDeleteTextChange = viewModel::onDeleteTextChanged,
                onPasswordChange = viewModel::onPasswordChanged,

            )
            DeletePasswordModalFooter(
                step = uiState.step,
                isValid = uiState.isFormValid,
                isLoading = uiState.submissionState === AuthSubmissionState.Loading,
                goToNextStep = {viewModel.onGoToNextStep()},
                goToPrevStep = {viewModel.onGoToPrevStep()}
            )
        }
    }
}

@Composable
fun DeletePasswordModalHeader(
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
                    .background(color = MaterialTheme.colorScheme.errorContainer, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.bin_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error
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
fun DeletePasswordModalContent(
    uiState: DeleteAccountUiState,
    onDeleteTextChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(Spacing.large)
            .verticalScroll(rememberScrollState())
    ) {
        if (uiState.step == 1) {
            DeleteStepOne()
        } else {
            DeleteStepTwo(
                deleteText = uiState.deleteText,
                onDeleteTextChange = onDeleteTextChange,
                password = uiState.password,
                onPasswordChange = onPasswordChange,
                isDeleteTextError = uiState.isDeleteTextError
            )
        }
    }
}

@Composable
fun DeleteStepOne() {
    Row(
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.errorContainer, shape = SplitWiseShapes.card)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.error, shape = SplitWiseShapes.card)
            .padding(ScreenDimensions.contentPadding)
    ) {
        Icon(
            painter = painterResource(R.drawable.caution_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
        Column {
            Text(
                text = stringResource(R.string.delete_alert_title),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.error
            )
            Spacer(Modifier.height(Spacing.small))
            Text(
                text = stringResource(R.string.delete_alert_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
    Spacer(Modifier.height(Spacing.large))
    Text(
        text = stringResource(R.string.delete_instr),
        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.SemiBold)
    )
    Spacer(Modifier.height(Spacing.small))
    val deleteInstructions = stringArrayResource(R.array.delete_instr_items)
    Column(verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)) {
        deleteInstructions.forEach {
            InstructionItem(text = it)
        }
    }
    Spacer(Modifier.height(Spacing.large))
    Column(
        modifier = Modifier
            .background(color = emerald_50, shape = SplitWiseShapes.card)
            .border(width = 1.dp, color = MaterialTheme.colorScheme.primary, shape = SplitWiseShapes.card)
            .padding(Spacing.medium)
    ) {
        Text(
            text = stringResource(R.string.delete_alt),
            style = MaterialTheme.typography.titleMedium,
            color = emerald_800
        )
        Spacer(Modifier.height(Spacing.small))
        val alternativeActions = stringArrayResource(R.array.delete_alt_items)
        Column(verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)) {
            alternativeActions.forEach {
                InstructionItem(text = it, color = emerald_700)
            }
        }
    }
}

@Composable
fun DeleteStepTwo(
    deleteText: String,
    isDeleteTextError: Boolean,
    onDeleteTextChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    val subtitleText = buildAnnotatedString {
        // 1. The word you want to style
        val styledWord = "DELETE"

        // 2. The full string from resources, with the placeholder
        val formatString = stringResource(id = R.string.confirm_deletion_subtitle, styledWord)

        // 3. Find the start and end index of the styled word within the full string
        val startIndex = formatString.indexOf(styledWord)
        val endIndex = startIndex + styledWord.length

        // 4. Append the full string
        append(formatString)

        // 5. Add the bold and red style to the specific part of the string
        if (startIndex != -1) {
            addStyle(
                style = SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.error // Use your theme's red color
                ),
                start = startIndex,
                end = endIndex
            )
        }
    }
    Text(
        text = subtitleText,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurface
    )
    Spacer(Modifier.height(Spacing.large))
    AppTextField(
        value = deleteText,
        onValueChange = onDeleteTextChange,
        label = stringResource(R.string.type_delete),
        placeholder = stringResource(R.string.type_delete_placeholder),
        isError = deleteText.isNotEmpty() && isDeleteTextError,
        errorMessage = if (deleteText.isNotEmpty() && isDeleteTextError) stringResource(R.string.type_delete_error) else null

    )
    AppTextField(
        value = password,
        onValueChange = onPasswordChange,
        label = stringResource(R.string.enter_your_password),
        placeholder = stringResource(R.string.enter_your_password),
        isPassword = true
    )
    Row(
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = Modifier
            .background(color = black, shape = SplitWiseShapes.card)
            .padding(ScreenDimensions.contentPadding)
    ) {
        Icon(
            painter = painterResource(R.drawable.caution_icon),
            contentDescription = null,
            tint = white
        )
        Column {
            Text(
                text = stringResource(R.string.last_warning),
                style = MaterialTheme.typography.titleMedium,
                color = white
            )
            Spacer(Modifier.height(Spacing.small))
            Text(
                text = stringResource(R.string.last_warning_desc),
                style = MaterialTheme.typography.bodyMedium,
                color = white
            )
        }
    }
}

@Composable
fun InstructionItem(
    text: String,
    modifier: Modifier = Modifier,
    color: Color? = null
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(color = color ?: MaterialTheme.colorScheme.error, shape = CircleShape)
        )
        Text(
            text = buildAnnotatedString {
                val parts = text.split(" - ", limit = 2)
                if (parts.size == 2) {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.SemiBold,
                        )
                    ) {
                        append(parts[0])
                    }
                    append(" - ")
                    append(parts[1])
                } else {
                    append(text)
                }
            },
            style = MaterialTheme.typography.bodySmall,
            color = color ?: MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun DeletePasswordModalFooter(
    step: Int,
    goToPrevStep: () -> Unit,
    goToNextStep: () -> Unit,
    isValid: Boolean,
    isLoading: Boolean,
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
            title = stringResource(if (step == 1) R.string.keep_account else R.string.go_back),
            onClick = {goToPrevStep()},
            containerColor = MaterialTheme.colorScheme.inverseSurface,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            modifier = Modifier
                .weight(1f)
        )
//        AppTextButton(
//            title = stringResource(if (step == 1) R.string.continue_delete else R.string.delete_my_account),
//            onClick = {goToNextStep()},
//            containerColor = MaterialTheme.colorScheme.error,
//            contentColor = MaterialTheme.colorScheme.onError,
//            enabled = isValid,
//            modifier = Modifier
//                .weight(1f)
//        )
        TextButton(
            onClick = {goToNextStep()},
            modifier = Modifier
                .weight(1f),
            enabled = isValid,
            colors = ButtonDefaults.textButtonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                disabledContainerColor = MaterialTheme.extendedColorScheme.disabledContainer,
                disabledContentColor = MaterialTheme.extendedColorScheme.onDisabledContainer
            ),
            shape = SplitWiseShapes.button,
            contentPadding = PaddingValues(vertical = ScreenDimensions.verticalPadding),
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.size(24.dp), // Approx height of text
                    color = MaterialTheme.colorScheme.onPrimary,
                    strokeWidth = 2.dp
                )
            } else {
                Text(
                    text = stringResource(if (step == 1) R.string.continue_delete else R.string.delete_my_account),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}