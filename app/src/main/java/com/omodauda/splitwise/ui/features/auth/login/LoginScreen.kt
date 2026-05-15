package com.omodauda.splitwise.ui.features.auth.login

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.GoogleAuthRequest
import com.omodauda.splitwise.data.network.model.LoginRequest
import com.omodauda.splitwise.model.LoginFormState
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
fun LoginScreen(
    authViewModel: AuthViewModel,
    toastHostState: ToastHostState,
    goToSignup: () -> Unit,
    goToForgotPassword: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = viewModel(),
) {
    val configuration = LocalConfiguration.current
    val isLandscape = configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val authState by authViewModel.loginUiState.collectAsStateWithLifecycle()
    val googleAuthUiState by authViewModel.googleAuthUiState.collectAsStateWithLifecycle()
    val scrollState = rememberScrollState()

    fun handleLogin() {
        if (viewModel.validateForm()) {
            authViewModel.login(
                data = LoginRequest(email = state.email, password = state.password)
            )
        }
    }

    fun handleContinueWithGoogle(idToken: String) {
        authViewModel.continueWithGoogle(data = GoogleAuthRequest(idToken))
    }

    if (authState.submissionState is AuthSubmissionState.Loading || googleAuthUiState.submissionState is AuthSubmissionState.Loading) {
        LoadingView()
    }

    LaunchedEffect(authState.submissionState) {
        val submissionState = authState.submissionState
        if (submissionState is AuthSubmissionState.Error) {
            toastHostState.showToast(
                toast = ToastState(
                    message = submissionState.message,
                    type = ToastType.ERROR
                )
            )
            authViewModel.resetLoginSubmissionState()
        }
    }

    LaunchedEffect(googleAuthUiState.submissionState) {
        val submissionState = googleAuthUiState.submissionState
        if (submissionState is AuthSubmissionState.Error) {
            toastHostState.showToast(
                toast = ToastState(
                    message = submissionState.message,
                    type = ToastType.ERROR
                )
            )
            authViewModel.resetGoogleAuthState()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize()
    ) { innerPadding ->
        if (!isLandscape) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.primary)
            ) {
                LoginHeader(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 46.dp, end = 46.dp, top = 88.dp, bottom = 73.dp)
                )
                LoginForm(
                    state = state,
                    onEmailChanged = viewModel::onEmailChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onLoginClick = ::handleLogin,
                    onForgotPasswordClick = goToForgotPassword,
                    onSignupClick = goToSignup,
                    onGoogleLoginClick = ::handleContinueWithGoogle,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            color = MaterialTheme.colorScheme.background,
                            shape = SplitWiseShapes.bottomSheet
                        )
                        .padding(
//                            top = Spacing.extraLarge,
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
                LoginHeader(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(0.4f)
                        .background(color = MaterialTheme.colorScheme.primary)
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.Center
                )
                LoginForm(
                    state = state,
                    onEmailChanged = viewModel::onEmailChanged,
                    onPasswordChanged = viewModel::onPasswordChanged,
                    onLoginClick = ::handleLogin,
                    onForgotPasswordClick = goToForgotPassword,
                    onSignupClick = goToSignup,
                    onGoogleLoginClick = ::handleContinueWithGoogle,
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
private fun LoginHeader(
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
}

@Composable
private fun LoginForm(
    state: LoginFormState,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    onLoginClick: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onSignupClick: () -> Unit,
    onGoogleLoginClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    verticalArrangement: Arrangement.Vertical = Arrangement.Top
) {
    Column(
        modifier = modifier,
        verticalArrangement = verticalArrangement
    ) {
        Spacer(Modifier.height(Spacing.extraLarge))
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
        TextButton(
            onClick = onForgotPasswordClick,
            modifier = Modifier.align(alignment = Alignment.End)
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
            onClick = onLoginClick
        )
        Spacer(Modifier.height(ScreenDimensions.largePadding))
        DividerView()
        Spacer(Modifier.height(ScreenDimensions.largePadding))
        AlternativeLoginView(
            onSignupClick,
            continueWithGoogle = onGoogleLoginClick
        )
        Spacer(Modifier.height(Spacing.extraLarge))
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
    continueWithGoogle: (idToken: String) -> Unit,
) {
    val annotatedString = buildAnnotatedString {
        append(stringResource(R.string.no_account) + " ")

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
                    goToSignup()
                }
            )
        ) {
            append(stringResource(R.string.sign_up))
        }
    }
    GoogleButton(
        continueWithGoogle = { idToken ->
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
fun LoginScreenPreview() {
//    val toastHostState = rememberToastHostState()
    SplitWiseTheme {
//        LoginScreen(goToSignup = {}, goToForgotPassword = {}, authViewModel = vm, toastHostState = toastHostState)
    }
}