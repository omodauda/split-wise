package com.omodauda.splitwise.ui.features.main.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowSizeClass
import com.omodauda.splitwise.R
import com.omodauda.splitwise.ui.features.auth.AuthViewModel
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseShapes
import com.omodauda.splitwise.ui.theme.SplitWiseTheme
import com.omodauda.splitwise.ui.theme.emerald_500
import java.time.Year

@Composable
fun ProfileScreen(
    adaptiveInfo: WindowAdaptiveInfo,
    authViewModel: AuthViewModel,
    goToAccountSettings: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val user by authViewModel.user.collectAsStateWithLifecycle()
    val isExpandedWidth = adaptiveInfo.windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.inverseOnSurface)
                .padding(start = innerPadding.calculateStartPadding(LayoutDirection.Ltr), end = innerPadding.calculateEndPadding(
                    LayoutDirection.Ltr), bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            ProfileHeader(
                paddingTop = innerPadding.calculateTopPadding(),
                fullName = user?.fullName,
                email = user?.email
            )
            ProfileContent(
                isExpandedWidth = isExpandedWidth,
                onLogout = {authViewModel.logout()},
                goToAccountSettings = goToAccountSettings
            )
        }
    }
}

@Composable
fun ProfileHeader(
    paddingTop: Dp,
    fullName: String?,
    email: String?,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.primary)
            .padding(start = Spacing.large, end = Spacing.large, top = paddingTop + ScreenDimensions.verticalPadding, bottom = ScreenDimensions.verticalPadding)
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
                    text = fullName ?: "",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(Modifier.height(Spacing.extraSmall))
                Text(
                    text = email ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
fun ProfileContent(
    isExpandedWidth: Boolean,
    goToAccountSettings: () -> Unit,
    onLogout: () -> Unit,
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
        if (!isExpandedWidth) {
            Column(modifier = Modifier.fillMaxSize()) {
                ProfileItem(
                    title = R.string.account_settings,
                    subTitle = R.string.account_settings_desc,
                    icon = R.drawable.settings_icon,
                    modifier = Modifier
                        .clickable(enabled = true, onClick = {goToAccountSettings()})
                )
                Spacer(Modifier.height(Spacing.medium))
//                ProfileItem(
//                    title = R.string.help_support,
//                    subTitle = R.string.help_desc,
//                    icon = R.drawable.help_icon,
//
//                    )
                Spacer(Modifier.height(Spacing.medium))
                ProfileItem(
                    title = R.string.logout,
                    icon = R.drawable.logout_icon,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .clickable(
                            enabled = true,
                            onClick = {onLogout()}
                        )
                )
            }
        } else {
            FlowRow(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
                verticalArrangement = Arrangement.spacedBy(Spacing.medium),
                maxItemsInEachRow = 2
            ) {
                ProfileItem(
                    title = R.string.account_settings,
                    subTitle = R.string.account_settings_desc,
                    icon = R.drawable.settings_icon,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = true, onClick = { goToAccountSettings() })
                )
//                ProfileItem(
//                    title = R.string.help_support,
//                    subTitle = R.string.help_desc,
//                    icon = R.drawable.help_icon,
//                    modifier = Modifier.weight(1f)
//                )
                ProfileItem(
                    title = R.string.logout,
                    icon = R.drawable.logout_icon,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier
                        .weight(1f)
                        .clickable(enabled = true, onClick = { onLogout() })
                )
//                Spacer(modifier = Modifier.weight(1f))
            }
        }

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
    icon: Int,
    modifier: Modifier = Modifier,
    subTitle: Int? = null,
    color: Color? = null,
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = 1.dp, shape = SplitWiseShapes.card)
            .background(color = MaterialTheme.colorScheme.background, shape = SplitWiseShapes.card)
            .padding(Spacing.medium)
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

    }
}