package com.example.splitwise.ui.features.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.emerald_500
import java.time.Year

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            ProfileHeader()
            ProfileContent()
        }
    }
}

@Composable
fun ProfileHeader(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .systemBarsPadding()
            .padding(horizontal = Spacing.large, vertical = ScreenDimensions.verticalPadding)
    ) {
        Text(
            text = stringResource(R.string.profile),
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.onPrimary
        )
        Spacer(Modifier.height(Spacing.large))
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = emerald_500, shape = MaterialTheme.shapes.large)
                .padding(Spacing.medium)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(64.dp)
                    .background(color = MaterialTheme.colorScheme.background, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(R.drawable.user_icon),
                    contentDescription = "user icon",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
            Column {
                Text(
                    text = "Lawal Dauda",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(Spacing.extraSmall))
                Text(
                    text = "omodauda.dl@gmail.com",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun ProfileContent(
    modifier: Modifier = Modifier
) {
    val currentYear = Year.now().value
    val context = LocalContext.current
    val appVersion = remember {
        try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            packageInfo.versionName
        }catch (e: Exception) {

        }
    }
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(vertical = ScreenDimensions.verticalPadding, horizontal = ScreenDimensions.contentPadding)
    ) {
        ProfileItem(
            title = R.string.account_settings,
            subTitle = R.string.account_settings_desc,
            icon = R.drawable.settings_icon,
            onClick = {}
        )
        Spacer(Modifier.height(Spacing.medium))
        ProfileItem(
            title = R.string.notifications,
            subTitle = R.string.notifications_desc,
            icon = R.drawable.notification_icon,
            onClick = {}
        )
        Spacer(Modifier.height(Spacing.medium))
        ProfileItem(
            title = R.string.help_support,
            subTitle = R.string.help_desc,
            icon = R.drawable.help_icon,
            onClick = {}
        )
        Spacer(Modifier.height(Spacing.medium))
        ProfileItem(
            title = R.string.logout,
            icon = R.drawable.logout_icon,
            color = MaterialTheme.colorScheme.error,
            onClick = {}
        )
        Spacer(Modifier.height(Spacing.extraLarge))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
        ) {
            Text(
                text = stringResource(R.string.app_version, appVersion.toString()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(Spacing.extraSmall))
            Text(
                text = stringResource(R.string.trademark, currentYear.toString()),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
fun ProfileItem(
    title: Int,
    subTitle: Int? = null,
    icon: Int,
    onClick: () -> Unit,
    color: Color? = null,
    modifier: Modifier = Modifier
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = SplitWiseShapes.card)
            .background(color = MaterialTheme.colorScheme.background, shape = SplitWiseShapes.card)
            .padding(Spacing.medium)
            .clickable(
                enabled = true,
                onClick = {onClick()}
            )
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(40.dp)
                    .background(color = if (color !== null) MaterialTheme.colorScheme.errorContainer else MaterialTheme.colorScheme.surfaceVariant, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(icon),
                    contentDescription = null,
                    tint =  color ?: MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Column {
                Text(
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                if (subTitle !== null) {
                    Spacer(Modifier.height(1.dp))
                    Text(
                        text = stringResource(subTitle),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Icon(
            painter = painterResource(R.drawable.forward_icon),
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun ProfileScreenPreview() {
    SplitWiseTheme {
        ProfileScreen()
    }
}