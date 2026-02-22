package com.example.splitwise.ui.components.toast

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun AppToast(
    toast: ToastState,
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier
) {
    val (icon, tint) = when (toast.type) {
        ToastType.SUCCESS -> Icons.Outlined.CheckCircle to MaterialTheme.colorScheme.primary
        ToastType.ERROR -> Icons.Outlined.Clear to MaterialTheme.colorScheme.error
        ToastType.INFO -> Icons.Outlined.Warning to MaterialTheme.colorScheme.secondary
    }
    
    Card(
        modifier = modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        ),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.background, shape = MaterialTheme.shapes.small)
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = tint,
                    modifier = Modifier
                        .size(20.dp)
                )
            }
            Text(
                text = toast.message,
                style = MaterialTheme.typography.bodyLarge,
                    color = tint,
                softWrap = true
            )
//            if (toast.actionLabel != null && toast.onAction != null) {
//                TextButton(
//                    onClick = {
//                        toast.onAction()
//                        onDismiss()
//                    }
//                ) {
//                    Text(
//                        text = toast.actionLabel,
//                        style = MaterialTheme.typography.bodyLarge,
////                        color = AppColors.Light.cyan,
//                    )
//                }
//            }
        }
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
fun AppToastPreview() {
    SplitWiseTheme {
        AppToast(
            toast = ToastState(
                message = "The product has been added to cart",
                actionLabel = "View Cart",
                onAction = {},
                type = ToastType.INFO
            ),
            onDismiss = {}
        )
    }
}