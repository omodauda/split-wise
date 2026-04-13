package com.omodauda.splitwise.ui.features.main.home.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.omodauda.splitwise.ui.theme.ComponentDimensions
import com.omodauda.splitwise.ui.theme.Elevation
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.valentinilk.shimmer.shimmer

@Composable
fun BillSectionShimmer(
    @StringRes titleRes: Int,
    @DrawableRes iconRes: Int,
    itemCount: Int = 5,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        BillSectionHeader(
            title = titleRes,
            icon = iconRes
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        Column(
            modifier = Modifier

                .shadow(
                    elevation = Elevation.level1,
                    shape = MaterialTheme.shapes.large
                )
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.large
                )
                .shimmer()
        ) {
            repeat(itemCount) { index ->
                BillItemShimmer()
                if (index < itemCount - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.surfaceVariant)
                    )
                }
            }
        }
    }
}

@Composable
private fun BillItemShimmer() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(Spacing.medium),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.medium)
    ) {
        // Avatar Circle
        Box(
            modifier = Modifier
                .size(ComponentDimensions.iconSizeExtraLarge)
                .background(color = MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(Spacing.extraSmall)
        ) {
            // Title line
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    )
            )
            // Subtitle line
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.3f)
                    .height(12.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        shape = MaterialTheme.shapes.small
                    )
            )
        }

        // Amount Box
        Box(
            modifier = Modifier
                .size(width = 60.dp, height = 24.dp)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.small
                )
        )
    }
}