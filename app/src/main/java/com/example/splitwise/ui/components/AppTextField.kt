package com.example.splitwise.ui.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    readOnly: Boolean? = false,
    label: String? = null,
    placeholder: String? = null,
    @DrawableRes leadingIcon: Int? = null,
    trailingIcon: (@Composable () -> Unit)? = null,
    isPassword: Boolean = false,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    isError: Boolean = false,
    errorMessage: String? = null,
    modifier: Modifier = Modifier,
    enabled: Boolean? = true
) {
    var isFocused by remember { mutableStateOf(false
    )}
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    val visualTransformation = if (isPassword && !passwordVisible) PasswordVisualTransformation() else VisualTransformation.None

    Column(
        modifier = modifier
            .padding(bottom = Spacing.extraMedium)
    ) {
        if (!label.isNullOrEmpty()) {
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                        append(label)
                    }
                },
                style = MaterialTheme.typography.bodyMedium,
            )
            Spacer(Modifier.height(Spacing.small))
        }
        OutlinedTextField(
            value,
            onValueChange,
            readOnly = readOnly ?: false,
            placeholder = { if (!placeholder.isNullOrEmpty()) Text(text = placeholder, color = MaterialTheme.colorScheme.onSurfaceVariant)},
            leadingIcon = if (leadingIcon != null) {
                {
                    Icon(
                        painter = painterResource(leadingIcon),
                        contentDescription = "username icon",
                        tint = if (!isFocused) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.primary
                    )
                }
            } else {
                null
            },
            trailingIcon = {
                when {
                    isPassword -> {
                        val icon = if (passwordVisible) R.drawable.visibility_icon else R.drawable.visibility_off_icon
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                painter = painterResource(icon),
                                contentDescription = "Toggle password visibility",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    trailingIcon != null -> trailingIcon()
                }
            },
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            singleLine = true,
            shape = SplitWiseShapes.inputField,
            isError = isError,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                focusedBorderColor = MaterialTheme.colorScheme.primary
//                focusedBorderColor = MaterialTheme.colorScheme.primary,
//                unfocusedBorderColor = MaterialTheme.colorScheme.inverseOnSurface,
//                cursorColor = MaterialTheme.colorScheme.primary,
//                focusedContainerColor = MaterialTheme.colorScheme.surfaceContainer,
//                unfocusedContainerColor = MaterialTheme.colorScheme.surfaceContainer
            ),
            enabled = enabled == true,
            modifier = Modifier.fillMaxWidth().onFocusChanged {focusState -> isFocused = focusState.isFocused}
        )
        if (isError && !errorMessage.isNullOrEmpty()) {
            Spacer(Modifier.height(Spacing.extraSmall))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Preview
@Composable
fun AppTextFieldPreview() {
    SplitWiseTheme {
        AppTextField(
            value = "",
            onValueChange = {},
            label = "hello",
            placeholder = "Enter email",
//            leadingIcon = R.drawable.visibility_icon,
        )
    }
}