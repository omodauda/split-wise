package com.omodauda.splitwise.ui.components

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialCancellationException
import androidx.credentials.exceptions.GetCredentialCustomException
import androidx.credentials.exceptions.NoCredentialException
import androidx.credentials.exceptions.GetCredentialException
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import com.omodauda.splitwise.BuildConfig
import com.omodauda.splitwise.R
import com.omodauda.splitwise.ui.theme.ComponentDimensions
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.SplitWiseTheme
import kotlinx.coroutines.launch
import java.security.SecureRandom
import java.util.Base64

@Composable
fun GoogleButton(
    continueWithGoogle: (idToken: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    fun generateSecureRandomNonce(byteLength: Int = 32): String {
        val randomBytes = ByteArray(byteLength)
        SecureRandom.getInstanceStrong().nextBytes(randomBytes)
        return Base64.getUrlEncoder().withoutPadding().encodeToString(randomBytes)
    }

    suspend fun signIn(request: GetCredentialRequest, context: Context): Exception? {
        val credentialManager = CredentialManager.create(context)
        val failureMessage = "Sign in failed!"
        val tag = "xyz"
        var caughtException: Exception? = null
        try {
            val result = credentialManager.getCredential(
                context,
                request
            )
            val credential = result.credential
            // Check if the credential received is a Google ID Token
            if (credential is CustomCredential &&
                credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
            ) {

                try {
                    // Parse the credential data into a GoogleIdTokenCredential object
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)

                    val idToken = googleIdTokenCredential.idToken

                    // Send idToken to your backend API for authentication
                    continueWithGoogle(idToken)

                } catch (e: GoogleIdTokenParsingException) {
                    Log.e(tag, "Received invalid Google ID token response", e)
                    caughtException = e
                }
            } else {
                Log.e(tag, "Unexpected credential type: ${credential.type}")
            }
        } catch (e: NoCredentialException) {
            Toast.makeText(context, "No Google accounts found", Toast.LENGTH_SHORT).show()
            Log.e(tag, "$failureMessage: No credentials found", e)
            caughtException = e

        } catch (e: GetCredentialCancellationException) {
            Toast.makeText(context, "Sign-in cancelled", Toast.LENGTH_SHORT).show()
            Log.e(tag, "$failureMessage: Sign-in was cancelled", e)
            caughtException = e

        } catch (e: GetCredentialCustomException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(tag, "$failureMessage: Issue with custom credential request", e)
            caughtException = e

        } catch (e: GetCredentialException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(tag, "$failureMessage: Failure getting credentials", e)
            caughtException = e

        } catch (e: GoogleIdTokenParsingException) {
            Toast.makeText(context, failureMessage, Toast.LENGTH_SHORT).show()
            Log.e(tag, "$failureMessage: Issue with parsing received GoogleIdToken", e)
            caughtException = e
        }
        return caughtException
    }

    val onClick: () -> Unit = {
        if (BuildConfig.GOOGLE_SERVER_CLIENT_ID.isEmpty()) {
            Log.e("xyz", "Google Server Client ID is empty. Check your local.properties.")
            Toast.makeText(context, "Sign-in configuration error", Toast.LENGTH_SHORT).show()
        } else {
            val googleIdOption: GetGoogleIdOption = GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(BuildConfig.GOOGLE_SERVER_CLIENT_ID)
                .setAutoSelectEnabled(false)
                .setNonce(generateSecureRandomNonce())
                .build()

            val request: GetCredentialRequest = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build()

            coroutineScope.launch {
                signIn(request, context)
            }
        }
    }


    Button(
        onClick = { onClick() },
        shape = SplitWiseShapes.button,
        border = BorderStroke(
            ComponentDimensions.borderWidthThin,
            MaterialTheme.colorScheme.outlineVariant
        ),
        contentPadding = PaddingValues(vertical = ScreenDimensions.verticalPadding),
        colors = ButtonColors(
            containerColor = MaterialTheme.colorScheme.inverseOnSurface,
            contentColor = MaterialTheme.colorScheme.inverseSurface,
            disabledContainerColor = MaterialTheme.colorScheme.inverseSurface,
            disabledContentColor = MaterialTheme.colorScheme.inverseSurface
        ),
        modifier = modifier
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(R.drawable.google_icon),
            contentDescription = "google icon"
        )
        Spacer(Modifier.width(Spacing.small))
        Text(
            text = stringResource(R.string.google),
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(
    name = "Light mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Composable
fun GoogleButtonPreview() {
    SplitWiseTheme {
        GoogleButton(
            continueWithGoogle = {}
        )
    }
}