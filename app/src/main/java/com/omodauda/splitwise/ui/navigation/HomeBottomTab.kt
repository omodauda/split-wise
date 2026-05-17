package com.omodauda.splitwise.ui.navigation

//import com.example.splitwise.ui.features.main.groups.GroupScreen
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.WindowAdaptiveInfo
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteDefaults
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteType
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.window.core.layout.WindowSizeClass
import com.omodauda.splitwise.R
import com.omodauda.splitwise.ui.components.toast.ToastHostState
import com.omodauda.splitwise.ui.features.auth.AuthViewModel
import com.omodauda.splitwise.ui.features.main.activity.ActivityScreen
import com.omodauda.splitwise.ui.features.main.activity.ActivityViewModel
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentViewModel
import com.omodauda.splitwise.ui.features.main.friends.FriendScreen
import com.omodauda.splitwise.ui.features.main.friends.FriendViewModel
import com.omodauda.splitwise.ui.features.main.home.BillsViewModel
import com.omodauda.splitwise.ui.features.main.home.HomeScreen
import com.omodauda.splitwise.ui.features.main.invites.InviteViewModel
import com.omodauda.splitwise.ui.features.main.paymentConfirmations.PaymentPendingConfirmationViewModel
import com.omodauda.splitwise.ui.features.main.profile.ProfileScreen

@Composable
fun HomeBottomTab(
    navController: NavController,
    adaptiveInfo: WindowAdaptiveInfo,
    authViewModel: AuthViewModel,
    inviteViewModel: InviteViewModel,
    friendViewModel: FriendViewModel,
    billsViewModel: BillsViewModel,
    activityViewModel: ActivityViewModel,
    pendingConfirmationViewModel: PaymentPendingConfirmationViewModel,
    confirmPaymentViewModel: ConfirmPaymentViewModel,
    toastHostState: ToastHostState
) {

    var currentTab by rememberSaveable { mutableStateOf(Screen.Home.route) }

    val items = listOf(
        Screen.Home,
//        Screen.Groups,
        Screen.Activity,
        Screen.Friends,
        Screen.Profile
    )

    val windowSize = adaptiveInfo.windowSizeClass

    // Compact height (< 480dp) -> Use Bottom Bar (Phone in Landscape)
    val isCompactLandscape =
        !windowSize.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)
    // Medium width (600dp - 839dp) -> Use Collapsed Rail (Tablets in Portrait)
    val isMediumWidth =
        windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_MEDIUM_LOWER_BOUND)
    // Expanded width (840dp+) -> Always show the Expanded Rail (Tablets/Desktops)
    val isExpandedWidth =
        windowSize.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)

    val layoutType = when {
        isCompactLandscape ->
            NavigationSuiteType.NavigationBar
        isExpandedWidth ->
            NavigationSuiteType.WideNavigationRailExpanded
        isMediumWidth ->
            NavigationSuiteType.NavigationRail
        else -> NavigationSuiteType.NavigationBar
    }

    val showLabel =
        layoutType == NavigationSuiteType.NavigationBar || layoutType == NavigationSuiteType.WideNavigationRailExpanded

    val myNavigationSuiteItemColors = NavigationSuiteDefaults.itemColors(
        navigationBarItemColors = NavigationBarItemDefaults.colors(
            indicatorColor = Color.Transparent,
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
        ),
        navigationRailItemColors = NavigationRailItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
            indicatorColor = Color.Transparent
        ),
        navigationDrawerItemColors = NavigationDrawerItemDefaults.colors(
            selectedIconColor = MaterialTheme.colorScheme.primary,
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant
        )
    )

    NavigationSuiteScaffold(
        layoutType = layoutType,
        navigationSuiteItems = {
            items.forEach { screen ->
                val selected = currentTab == screen.route
                item(
                    selected = selected,
                    onClick = { currentTab = screen.route },
                    icon = {
                        val iconRes = when (screen) {
                            Screen.Home -> R.drawable.home_icon
                            Screen.Activity -> R.drawable.activity_icon
                            Screen.Friends -> R.drawable.friends_icon
                            Screen.Profile -> R.drawable.profile_icon
                            else -> R.drawable.home_icon
                        }
                        Icon(
                            painter = painterResource(iconRes),
                            contentDescription = null,
                            tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    },
                    colors = myNavigationSuiteItemColors,
                    label = if (showLabel) {
                        { Text(text = screen.route.replaceFirstChar { it.uppercase() }) }
                    } else null
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.background,
        navigationSuiteColors = NavigationSuiteDefaults.colors(
            shortNavigationBarContainerColor = MaterialTheme.colorScheme.background,
            shortNavigationBarContentColor = MaterialTheme.colorScheme.background,
            navigationBarContainerColor = MaterialTheme.colorScheme.background,
            navigationRailContainerColor = MaterialTheme.colorScheme.background,
        )
    ) {
        when (currentTab) {
            Screen.Home.route -> HomeScreen(
                goToAddBill = { navController.navigate(Screen.AddBill.route) },
                inviteViewModel,
                friendViewModel,
                billsViewModel,
                authViewModel,
                toastHostState,
                viewAllOwedBills = { navController.navigate(Screen.OwedBillList.route) },
                viewAllOwingBills = { navController.navigate(Screen.OwingBillList.route) },
                onBillItemClicked = { billId ->
                    navController.navigate(Screen.BillDetails.createRoute(billId))
                },
                goToPaymentConfirmation = { navController.navigate(Screen.PaymentConfirmation.route) },
                paymentPendingConfirmationViewModel = pendingConfirmationViewModel,
                confirmPaymentViewModel = confirmPaymentViewModel
            )

            Screen.Activity.route -> ActivityScreen(viewModel = activityViewModel)
            Screen.Friends.route -> FriendScreen(viewModel = friendViewModel, inviteViewModel)
            Screen.Profile.route -> ProfileScreen(
                authViewModel = authViewModel,
                goToAccountSettings = { navController.navigate(Screen.AccountSettings.route) }
            )
        }
    }
}