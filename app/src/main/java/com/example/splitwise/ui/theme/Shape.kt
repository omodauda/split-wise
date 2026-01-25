package com.example.splitwise.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val Shapes = Shapes(
    // Extra Small - Chips, small buttons
    extraSmall = RoundedCornerShape(4.dp),

    // Small - Buttons, input fields
    small = RoundedCornerShape(8.dp),

    // Medium - Cards, dialogs
    medium = RoundedCornerShape(12.dp),

    // Large - Sheets, large components
    large = RoundedCornerShape(16.dp),

    // Extra Large - Full screen dialogs
    extraLarge = RoundedCornerShape(28.dp)
)

// Custom shape tokens for specific components
object SplitWiseShapes {
    val button = RoundedCornerShape(16.dp)
    val buttonSmall = RoundedCornerShape(8.dp)
    val card = RoundedCornerShape(16.dp)
    val dialog = RoundedCornerShape(24.dp)
    val onboardingImage = RoundedCornerShape(24.dp)
    val bottomSheet = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp)
    val chip = RoundedCornerShape(8.dp)
    val inputField = RoundedCornerShape(16.dp)
    val avatar = RoundedCornerShape(50) // Circular
    val appIcon = RoundedCornerShape(24.dp)
    val badge = RoundedCornerShape(12.dp)
}