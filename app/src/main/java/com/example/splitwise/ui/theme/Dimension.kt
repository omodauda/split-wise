package com.example.splitwise.ui.theme

import androidx.compose.ui.unit.dp

/**
 * Spacing scale following Material 3 guidelines
 * Use these consistent spacing values throughout the app
 */
object Spacing {
    val none = 0.dp
    val extraSmall = 4.dp
    val small = 8.dp
    val medium = 16.dp
    val large = 24.dp
    val extraLarge = 32.dp
    val huge = 48.dp
    val massive = 64.dp
}

/**
 * Elevation scale for shadows and surfaces
 */
object Elevation {
    val none = 0.dp
    val level1 = 1.dp  // Cards at rest
    val level2 = 3.dp  // Cards raised, buttons
    val level3 = 6.dp  // FAB at rest, dialogs
    val level4 = 8.dp  // FAB raised
    val level5 = 12.dp // Navigation drawer, bottom sheet
}

/**
 * Component-specific dimensions
 */
object ComponentDimensions {
    // Button heights
    val buttonHeightLarge = 56.dp
    val buttonHeightMedium = 48.dp
    val buttonHeightSmall = 40.dp

    // Icon sizes
    val iconSizeSmall = 16.dp
    val iconSizeMedium = 24.dp
    val iconSizeLarge = 32.dp
    val iconSizeExtraLarge = 48.dp

    // Avatar sizes
    val avatarSizeSmall = 32.dp
    val avatarSizeMedium = 40.dp
    val avatarSizeLarge = 56.dp
    val avatarSizeExtraLarge = 80.dp

    // Input field heights
    val inputFieldHeight = 56.dp
    val inputFieldHeightSmall = 48.dp

    // Bottom navigation
    val bottomNavHeight = 80.dp

    // Top app bar
    val topAppBarHeight = 64.dp

    // Card minimum height
    val cardMinHeight = 80.dp

    // Divider thickness
    val dividerThickness = 1.dp

    // Border widths
    val borderWidthThin = 1.dp
    val borderWidthMedium = 2.dp
    val borderWidthThick = 4.dp
}

/**
 * Screen padding and margins
 */
object ScreenDimensions {
    val horizontalPadding = 16.dp
    val verticalPadding = 16.dp
    val contentPadding = 16.dp
    val sectionSpacing = 24.dp
    val itemSpacing = 12.dp
    val largePadding = 32.dp
}