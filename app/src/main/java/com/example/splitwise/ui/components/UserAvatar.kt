package com.example.splitwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.splitwise.ui.theme.ComponentDimensions

@Composable
fun UserAvatar(
    fullName: String,
    avatarUrl: String?,
    modifier: Modifier = Modifier
) {
    val avatarText = fullName[0].toString()
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(ComponentDimensions.iconSizeExtraLarge)
            .background(color = MaterialTheme.colorScheme.primary, shape = CircleShape)
    ) {
        if (avatarUrl === null) {
            Text(
                text = avatarText,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}