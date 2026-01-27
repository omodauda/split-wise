package com.example.splitwise.ui.features.auth.login

import android.content.res.Configuration
import android.util.Log
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppTextButton
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.components.GoogleButton
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun LoginScreen(
    viewModel: LoginViewModel = viewModel(),
    goToSignup: () -> Unit,
    goToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    fun handleLogin() {
        if (viewModel.validateForm()){
            Log.d("LOGIN", "handleLogin: proceed to login")
        }
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 46.dp, end = 46.dp, top = 88.dp, bottom = 73.dp)
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.background,
                            shape = SplitWiseShapes.appIcon
                        )
                        .padding(20.dp)
                ) {
                    Text(
                        text = "\uD83D\uDCB0",
                        style = MaterialTheme.typography.displaySmall,
                    )
                }
                Spacer(Modifier.height(Spacing.large))
                Text(
                    text = stringResource(R.string.login_title),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(Spacing.large))
                Text(
                    text = stringResource(R.string.login_description),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    textAlign = TextAlign.Center
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = SplitWiseShapes.bottomSheet
                    )
                    .padding(vertical = Spacing.extraLarge, horizontal = Spacing.large)
                    .verticalScroll(state = scrollState)
            ) {
                AppTextField(
                    value = state.email,
                    onValueChange = {
                        viewModel.onEmailChanged(it)
                                    },
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
                AppTextField(
                    value = state.password,
                    onValueChange = {
                        viewModel.onPasswordChanged(it)
                                    },
                    label = stringResource(R.string.password),
                    isPassword = true,
                    leadingIcon = R.drawable.password_icon,
                    placeholder = stringResource(R.string.password_placeholder),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        capitalization = KeyboardCapitalization.None,
                        imeAction = ImeAction.Done
                    ),
                    isError = state.passwordError != null,
                    errorMessage = state.passwordError
                )
                TextButton(
                    onClick = {goToForgotPassword()},
                    modifier = Modifier
                        .align(alignment = Alignment.End)
                ) {
                    Text(
                        text = stringResource(R.string.forgot_password),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                Spacer(Modifier.height(Spacing.extraMedium))
                AppTextButton(
                    title = stringResource(R.string.sign_in),
                    onClick = {handleLogin()}
                )
                Spacer(Modifier.height(ScreenDimensions.largePadding))
                DividerView()
                Spacer(Modifier.height(ScreenDimensions.largePadding))
                AlternativeLoginView(goToSignup)
            }
        }

    }
}

@Composable
fun DividerView(
    modifier: Modifier = Modifier
) {
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
            text = stringResource(R.string.continue_with),
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun AlternativeLoginView(
    goToSignup: () -> Unit,
    modifier: Modifier = Modifier
) {
    val annotatedString = buildAnnotatedString {
        // Append the first part of the text with the default style
        append(stringResource(R.string.no_account) + " ")

        // Use pushStringAnnotation to tag the clickable part
        pushStringAnnotation(tag = "SIGNUP", annotation = "signup")

        // Apply a custom style for the "Sign Up" part
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(stringResource(R.string.sign_up))
        }
        pop()
    }
    GoogleButton(onClicked = {})
    Spacer(Modifier.height(Spacing.extraLarge))
    ClickableText(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium.copy(textAlign = TextAlign.Center),
        modifier = Modifier.fillMaxWidth(),
        onClick = { offset ->
            annotatedString.getStringAnnotations(tag = "SIGNUP", start = offset, end = offset)
                .firstOrNull()?.let {
                    goToSignup()
                }
        }
    )
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
fun LoginScreenPreview() {
    SplitWiseTheme {
        LoginScreen(goToSignup = {}, goToForgotPassword = {})
    }
}