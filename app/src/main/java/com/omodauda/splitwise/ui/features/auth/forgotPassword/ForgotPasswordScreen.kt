package com.omodauda.splitwise.ui.features.auth.forgotPassword

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omodauda.splitwise.R
import com.omodauda.splitwise.model.ForgotPasswordFormState
import com.omodauda.splitwise.ui.components.AppTextButton
import com.omodauda.splitwise.ui.components.AppTextField
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.SplitWiseTheme

@Composable
fun ForgotPasswordScreen(
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: ForgotPasswordViewModel = viewModel(),

) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    fun handleSubmit() {
//        if (viewModel.validateForm()) {}
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->

        if (!isLandscape) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                ForgotPasswordHeader(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier
                        .weight(1f)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 35.dp)
                )
                ForgotPasswordForm(
                    state = state,
                    goBack = goBack,
                    onEmailChanged = viewModel::onEmailChanged,
                    onSubmit = {handleSubmit()},
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.background, shape = SplitWiseShapes.bottomSheet)
                        .padding(horizontal = 24.dp, vertical = 32.dp)
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = innerPadding.calculateStartPadding(LayoutDirection.Ltr), end = innerPadding.calculateEndPadding(
                        LayoutDirection.Ltr))
            ) {
                ForgotPasswordHeader(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.4f)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center
                )
                ForgotPasswordForm(
                    state = state,
                    goBack = goBack,
                    onEmailChanged = viewModel::onEmailChanged,
                    onSubmit = {handleSubmit()},
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                        .padding(horizontal = 24.dp)
                        .verticalScroll(state = rememberScrollState())
                    ,
                    verticalArrangement = Arrangement.Center
                )
            }
        }

    }
}

@Composable
private fun ForgotPasswordHeader(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top
) {
    Column(
        horizontalAlignment = horizontalAlignment,
        verticalArrangement =verticalArrangement,
        modifier = modifier
    ) {
        Box(
            modifier = Modifier
                .background(
                    MaterialTheme.colorScheme.background,
                    shape = SplitWiseShapes.appIcon
                )
                .padding(20.dp)
        ) {
            Image(
                painter = painterResource(R.drawable.email_logo),
                contentDescription = "email logo"
            )
        }
        Spacer(Modifier.height(Spacing.large))
        Text(
            text = stringResource(R.string.forgot_password),
            style = MaterialTheme.typography.displaySmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.height(Spacing.large))
        Text(
            text = stringResource(R.string.forgot_password_desc),
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ForgotPasswordForm(
    state: ForgotPasswordFormState,
    goBack: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onSubmit: () -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        AppTextField(
            value = state.email,
            onValueChange = onEmailChanged,
            label = stringResource(R.string.email),
            leadingIcon = R.drawable.email_icon,
            placeholder = stringResource(R.string.email_placeholder),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
            isError = state.emailError != null,
            errorMessage = state.emailError
        )
        AppTextButton(
            title = stringResource(R.string.send_link),
            onClick = onSubmit
        )
        Spacer(Modifier.height(38.dp))
        TextButton(
            onClick = {goBack()},
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
        ) {
            IconButton(
                onClick = { goBack() }
            ) {
                Icon(
                    painter = painterResource(R.drawable.back_icon),
                    contentDescription = "back icon",
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Text(
                text = stringResource(R.string.back_to_login),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)

@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun ForgotPasswordScreenPreview() {
    SplitWiseTheme {
        ForgotPasswordScreen(goBack = {})
    }
}