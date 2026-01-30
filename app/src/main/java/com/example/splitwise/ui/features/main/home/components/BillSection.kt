package com.example.splitwise.ui.features.main.home.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.theme.Elevation
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import java.util.Locale

@Composable
fun OwedView(
    openReminderModal: () -> Unit,
    modifier: Modifier = Modifier
){
    BillSection(
        titleRes = R.string.you_are_owed,
        iconRes = R.drawable.arrow_down,
        itemCount = 5,
        itemContent = { OwedItem( openReminderModal) },
        modifier = modifier
    )
}

@Composable
fun OwingView(
    modifier: Modifier = Modifier
) {
    BillSection(
        titleRes = R.string.you_owe,
        iconRes = R.drawable.arrow_up,
        itemCount = 1,
        itemContent = { OwingItem() },
        modifier = modifier
    )
}

/**
 * A generic, reusable component to display a section of bills (e.g., Owed or Owing).
 *
 * @param titleRes The string resource for the section header title.
 * @param iconRes The drawable resource for the section header icon.
 * @param itemCount The number of items to display in the section.
 * @param itemContent A composable lambda that defines the content for a single item.
 * @param modifier The modifier to be applied to the component.
 */
@Composable
private fun BillSection(
    @StringRes titleRes: Int,
    @DrawableRes iconRes: Int,
    itemCount: Int,
    itemContent: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        BillSectionHeader(
            title = titleRes,
            icon = iconRes
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        Column(
            modifier = Modifier // Use a fresh modifier, don't reuse the one from the parameter
                .shadow(
                    elevation = Elevation.level1,
                    shape = MaterialTheme.shapes.large
                )
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = MaterialTheme.shapes.large
                )
        ) {
            if (itemCount > 0) {
                repeat(itemCount) { index ->
                    itemContent()
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
}

@Composable
fun BillSectionHeader(
    title: Int,
    icon: Int,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(Spacing.small),
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(icon),
            contentDescription = null
        )
        Text(
            text = stringResource(title).uppercase(Locale.getDefault()),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}