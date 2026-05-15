package com.omodauda.splitwise.ui.features.auth.signup

import android.content.res.Configuration
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
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextLinkStyles
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withLink
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.GoogleAuthRequest
import com.omodauda.splitwise.data.network.model.SignupRequest
import com.omodauda.splitwise.model.SignupFormState
import com.omodauda.splitwise.ui.components.AppTextButton
import com.omodauda.splitwise.ui.components.AppTextField
import com.omodauda.splitwise.ui.components.GoogleButton
import com.omodauda.splitwise.ui.components.LoadingView
import com.omodauda.splitwise.ui.components.toast.ToastHostState
import com.omodauda.splitwise.ui.components.toast.ToastState
import com.omodauda.splitwise.ui.components.toast.ToastType
import com.omodauda.splitwise.ui.features.auth.AuthSubmissionState
import com.omodauda.splitwise.ui.features.auth.AuthViewModel
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.SplitWiseTheme

@Composable
fun SignupScreen(
    authViewModel: AuthViewModel,
    toastHostState: ToastHostState,
    goBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SignupViewModel = viewModel(),
) {
    val authUiState by authViewModel.signupUiState.collectAsStateWithLifecycle()
    val googleAuthUiState by authViewModel.googleAuthUiState.collectAsStateWithLifecycle()
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    fun handleSignup() {
        if (viewModel.validateForm()) {
            authViewModel.signup(
                data = SignupRequest(
                    fullName = state.fullName,
                    email = state.email,
                    password = state.password
                )
            )
        }
    }

    fun handleContinueWithGoogle(idToken: String) {
        authViewModel.continueWithGoogle(data = GoogleAuthRequest(idToken))
    }

    if (authUiState.submissionState is AuthSubmissionState.Loading || googleAuthUiState.submissionState is AuthSubmissionState.Loading) {
        LoadingView()
    }

    LaunchedEffect(authUiState.submissionState) {
        val state = authUiState.submissionState
        if (state is AuthSubmissionState.Error) {
            toastHostState.showToast(
                toast = ToastState(
                    message = state.message,
                    type = ToastType.ERROR
                )
            )
            authViewModel.resetSignupState()
        }
    }

    LaunchedEffect(googleAuthUiState.submissionState) {
        val state = googleAuthUiState.submissionState
        if (state is AuthSubmissionState.Error) {
            toastHostState.showToast(
                toast = ToastState(
                    message = state.message,
                    type = ToastType.ERROR
                )
            )
            authViewModel.resetGoogleAuthState()
        }
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
                SignupHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 46.dp, end = 46.dp, top = 88.dp, bottom = 73.dp)
                )
                SignupForm(
                    state = state,
                    onFullNameChanged = viewModel::onFullNameChanged,
                    onEmailChanged = viewModel::onEmailChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onConfirmPasswordChanged = viewModel::onPasswordChanged,
                    goBack = goBack,
                    handleSignup = {handleSignup()},
                    handleContinueWithGoogle = {handleContinueWithGoogle(it)},
                    modifier = Modifier
                        .weight(0.6f)
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = SplitWiseShapes.bottomSheet
                        )
                        .padding(
                            start = Spacing.large,
                            end = Spacing.large,
                            bottom = innerPadding.calculateBottomPadding()
                        )
                        .verticalScroll(state = scrollState)
                )
            }
        } else {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colorScheme.background)
                    .padding(
                        start = innerPadding.calculateStartPadding(LayoutDirection.Ltr),
                        end = innerPadding.calculateEndPadding(LayoutDirection.Ltr)
                    )
            ) {
                SignupHeader(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.4f)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center
                )
                SignupForm(
                    state = state,
                    onFullNameChanged = viewModel::onFullNameChanged,
                    onEmailChanged = viewModel::onEmailChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onConfirmPasswordChanged = viewModel::onPasswordChanged,
                    goBack = goBack,
                    handleSignup = {handleSignup()},
                    handleContinueWithGoogle = {handleContinueWithGoogle(it)},
                    modifier = Modifier
                        .weight(0.6f)
                        .fillMaxSize()
                        .background(color = MaterialTheme.colorScheme.background)
                        .padding(start = Spacing.large, end = Spacing.extraLarge)
                        .verticalScroll(state = scrollState),
                    verticalArrangement = Arrangement.Center
                )
            }
        }
    }
}

@Composable
private fun SignupHeader(
    modifier: Modifier = Modifier,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top
) {
        Column(
            horizontalAlignment = horizontalAlignment,
            verticalArrangement = verticalArrangement,
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

}

@Composable
private fun SignupForm(
    state: SignupFormState,
    goBack: () -> Unit,
    onFullNameChanged: (String) -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onConfirmPasswordChanged: (String) -> Unit,
    handleSignup: () -> Unit,
    handleContinueWithGoogle: (String) -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top
) {
    val annotatedText = buildAnnotatedString {
        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append("I agree to the")
        }

        withLink(
            LinkAnnotation.Url(
                url = "https://www.google.com",
                styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary))
            )
        ) {
            append(" Terms of Service")
        }

        withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
            append(" and ")
        }

        withLink(
            LinkAnnotation.Url(
                url = "https://www.facebook.com",
                styles = TextLinkStyles(style = SpanStyle(color = MaterialTheme.colorScheme.primary))
            )
        ) {
            append(" Privacy Policy")
        }
    }
    Column(
        verticalArrangement = verticalArrangement,
        modifier = modifier
    ) {
        Spacer(Modifier.height(Spacing.extraLarge))
        AppTextField(
            value = state.fullName,
            onValueChange = onFullNameChanged,
            label = stringResource(R.string.full_name),
            leadingIcon = R.drawable.email_icon,
            placeholder = stringResource(R.string.full_name_placeholder),
            keyboardOptions = KeyboardOptions(
                capitalization = KeyboardCapitalization.None,
                imeAction = ImeAction.Next
            ),
            isError = state.fullNameError != null,
            errorMessage = state.fullNameError
        )
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
        AppTextField(
            value = state.password,
            onValueChange = onPasswordChanged,
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
            onValueChange = onConfirmPasswordChanged,
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
        Text(
            text = annotatedText,
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            style = MaterialTheme.typography.bodyMedium
        )
        Spacer(Modifier.height(Spacing.medium))
        AppTextButton(
            title = stringResource(R.string.create_account),
            onClick = {handleSignup()}
        )
        Spacer(Modifier.height(ScreenDimensions.largePadding))
        DividerView()
        Spacer(Modifier.height(ScreenDimensions.largePadding))
        AlternativeSignupView(
            goToLogin = {goBack()},
            continueWithGoogle = {idToken ->
                handleContinueWithGoogle(idToken)
            }
        )
        Spacer(Modifier.height(Spacing.extraLarge))
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
            .padding(vertical = ScreenDimensions.itemSpacing)
    ) {
        Text(
            text = stringResource(R.string.password_guide),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(3.dp))
        repeat(items.size) {iteration ->
            Text(
                text = items[iteration],
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
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
fun AlternativeSignupView(
    goToLogin: () -> Unit,
    continueWithGoogle: (idToken: String) -> Unit
) {
    val annotatedString = buildAnnotatedString {
        // Append the first part of the text with the default style
        append(stringResource(R.string.existing_account) + " ")

        withLink(
            LinkAnnotation.Clickable(
                tag = "SIGNUP",
                styles = TextLinkStyles(
                    style = SpanStyle(
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                ),
                linkInteractionListener = {
                    goToLogin()
                }
            )
        ) {
            append(stringResource(R.string.sign_in))
        }
    }
    GoogleButton(
        continueWithGoogle = {idToken ->
            continueWithGoogle(idToken)
        }
    )
    Spacer(Modifier.height(Spacing.extraLarge))
    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodyMedium.copy(
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground
        ),
        modifier = Modifier.fillMaxWidth()
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
//        SignupScreen(goBack = {}, authViewModel = vm, toastHostState = toastHostState)
    }
}