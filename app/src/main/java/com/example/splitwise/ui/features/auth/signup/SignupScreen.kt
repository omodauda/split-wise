package com.example.splitwise.ui.features.auth.signup

import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringArrayResource
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
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppTextButton
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.components.GoogleButton
import com.example.splitwise.ui.features.auth.login.AlternativeLoginView
import com.example.splitwise.ui.features.auth.login.DividerView
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.emerald_50

@Composable
fun SignupScreen(
    viewModel: SignupViewModel = viewModel(),
    modifier: Modifier = Modifier
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val context = LocalContext.current
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append("I agree to the")
        }

        pushStringAnnotation(tag = "TERMS", annotation = "https://www.google.com")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(" Terms of Service")
        }
        pop()
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append(" and ")
        }
        pushStringAnnotation(tag = "PRIVACY", annotation = "https://www.facebook.com")
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
            append(" Privacy Policy")
        }
        pop()
    }

    fun handleSignup() {
        viewModel.validateForm()
    }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.primary)
                .padding(innerPadding)
//                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            IconButton(
                onClick = {}
            ) {
                Icon(
                    painter = painterResource(R.drawable.back_icon),
                    contentDescription = "back icon",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 46.dp, end = 46.dp, top = 0.dp, bottom = 25.dp)
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
                    text = stringResource(R.string.create_account),
                    style = MaterialTheme.typography.displaySmall,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(Spacing.large))
                Text(
                    text = stringResource(R.string.signup_description),
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
                    .padding(top = Spacing.extraLarge, start = Spacing.large, end = Spacing.large)
                    .verticalScroll(state = scrollState)
            ) {
                AppTextField(
                    value = state.fullName,
                    onValueChange = {
                        viewModel.onFullNameChanged(it)
                    },
                    label = stringResource(R.string.full_name),
                    leadingIcon = R.drawable.email_icon,
                    placeholder = stringResource(R.string.full_name_placeholder),
                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType,
                        capitalization = KeyboardCapitalization.None,
                        imeAction = ImeAction.Next
                    ),
                    isError = state.fullNameError != null,
                    errorMessage = state.fullNameError
                )
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
                AppTextField(
                    value = state.confirmPassword,
                    onValueChange = {
                        viewModel.onConfirmPasswordChanged(it)
                    },
                    label = stringResource(R.string.confirm_password),
                    isPassword = true,
                    leadingIcon = R.drawable.password_icon,
                    placeholder = stringResource(R.string.confirm_password_placeholder),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        capitalization = KeyboardCapitalization.None,
                        imeAction = ImeAction.Done
                    ),
                    isError = state.confirmPasswordError != null,
                    errorMessage = state.confirmPasswordError
                )

                Spacer(Modifier.height(Spacing.medium))
                // Password guide
                PasswordGuide()
                Spacer(Modifier.height(Spacing.medium))
                ClickableText(
                    text = annotatedText,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium,
                    onClick = { offset ->
                        annotatedText.getStringAnnotations("PRIVACY", offset, offset)
                            .firstOrNull()?.let { annotation ->
                                val intent = Intent(Intent.ACTION_VIEW, annotation.item.toUri())
                                context.startActivity(intent)
                            }

                        annotatedText.getStringAnnotations("TERMS", offset, offset)
                            .firstOrNull()?.let { annotation ->
                                val intent = Intent(Intent.ACTION_VIEW, annotation.item.toUri())
                                context.startActivity(intent)
                            }
                    }
                )
                Spacer(Modifier.height(Spacing.medium))
                AppTextButton(
                    title = stringResource(R.string.create_account),
                    onClick = {handleSignup()}
                )
                Spacer(Modifier.height(ScreenDimensions.largePadding))
                com.example.splitwise.ui.features.auth.signup.DividerView()
                Spacer(Modifier.height(ScreenDimensions.largePadding))
                AlternativeSignupView()
                Spacer(Modifier.height(Spacing.extraLarge))
            }
        }
    }
}

@Composable
fun PasswordGuide(
    modifier: Modifier = Modifier
) {
    val items = stringArrayResource(R.array.password_guide_items)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(color = emerald_50, shape = RoundedCornerShape(14.dp))
            .padding(ScreenDimensions.itemSpacing)
    ) {
        Text(
            text = stringResource(R.string.password_guide),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(Modifier.height(3.dp))
        repeat(items.size) {iteration ->
            Text(
                text = items[iteration],
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(Modifier.height(2.dp))
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
            text = stringResource(R.string.alt_signup),
            color = MaterialTheme.colorScheme.onBackground
        )
        HorizontalDivider(
            modifier = Modifier
                .weight(1f)
        )
    }
}

@Composable
fun AlternativeSignupView() {
    val annotatedString = buildAnnotatedString {
        // Append the first part of the text with the default style
        append(stringResource(R.string.existing_account) + " ")

        // Use pushStringAnnotation to tag the clickable part
        pushStringAnnotation(tag = "SIGN IN", annotation = "Sign In")

        // Apply a custom style for the "Sign Up" part
        withStyle(
            style = SpanStyle(
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        ) {
            append(stringResource(R.string.sign_in))
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
                .firstOrNull()?.let { annotation ->
                    // TODO: Handle sign up navigation
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
fun SignupScreenPreview() {
    SplitWiseTheme {
        SignupScreen()
    }
}