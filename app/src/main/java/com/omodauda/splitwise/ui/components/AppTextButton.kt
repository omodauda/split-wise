package com.omodauda.splitwise.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.omodauda.splitwise.ui.theme.ButtonLarge
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.SplitWiseTheme
import com.omodauda.splitwise.ui.theme.extendedColorScheme

@Composable
fun AppTextButton(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    containerColor: Color? = null,
    contentColor: Color? = null,
) {
    Button(
        onClick = { onClick() },
        colors = ButtonColors(
            containerColor = containerColor ?: MaterialTheme.colorScheme.primary,
            contentColor = contentColor ?: MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.extendedColorScheme.disabledContainer,
            disabledContentColor = MaterialTheme.extendedColorScheme.onDisabledContainer
        ),
        shape = SplitWiseShapes.button,
        contentPadding = PaddingValues(vertical = ScreenDimensions.verticalPadding),
        enabled = enabled && !isLoading,
        modifier = modifier
            .fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(20.dp),
                color = contentColor ?: MaterialTheme.colorScheme.onPrimary,
                strokeWidth = 2.dp
            )
        } else {
            Text(
                text = title,
                style = ButtonLarge
            )
        }
    }
}

@Preview(
    name = "Light mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun AppTextButtonPreview() {
    SplitWiseTheme {
        AppTextButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = {},
            title = "Button",
            enabled = true
        )
    }
}