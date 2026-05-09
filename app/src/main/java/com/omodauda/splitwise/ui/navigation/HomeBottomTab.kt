package com.omodauda.splitwise.ui.navigation

//import com.example.splitwise.ui.features.main.groups.GroupScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.omodauda.splitwise.R
import com.omodauda.splitwise.ui.components.toast.ToastHostState
import com.omodauda.splitwise.ui.features.auth.AuthViewModel
import com.omodauda.splitwise.ui.features.main.activity.ActivityScreen
import com.omodauda.splitwise.ui.features.main.activity.ActivityViewModel
import com.omodauda.splitwise.ui.features.main.friends.FriendScreen
import com.omodauda.splitwise.ui.features.main.friends.FriendViewModel
import com.omodauda.splitwise.ui.features.main.home.BillsViewModel
import com.omodauda.splitwise.ui.features.main.home.HomeScreen
import com.omodauda.splitwise.ui.features.main.invites.InviteViewModel
import com.omodauda.splitwise.ui.features.main.profile.ProfileScreen
import com.omodauda.splitwise.ui.theme.Elevation

@Composable
fun HomeBottomTab(
    navController: NavController,
    authViewModel: AuthViewModel,
    inviteViewModel: InviteViewModel,
    friendViewModel: FriendViewModel,
    billsViewModel: BillsViewModel,
    activityViewModel: ActivityViewModel,
    toastHostState: ToastHostState,
    startDestination: String = Screen.Home.route
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {BottomNavigationBar(bottomNavController)}
    ) { innerPadding ->
        NavHost(
            navController = bottomNavController,
            startDestination = startDestination,
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen(
                    goToAddBill = {navController.navigate(Screen.AddBill.route)},
                    inviteViewModel,
                    friendViewModel,
                    billsViewModel,
                    authViewModel,
                    toastHostState,
                    viewAllOwedBills = {navController.navigate(Screen.OwedBillList.route)},
                    viewAllOwingBills = {navController.navigate(Screen.OwingBillList.route)},
                    onBillItemClicked = { billId ->
                        navController.navigate(Screen.BillDetails.createRoute(billId))
                    },
                    goToPaymentConfirmation = {navController.navigate(Screen.PaymentConfirmation.route)}
                )

            }
//            composable(route = Screen.Groups.route) {
//                GroupScreen()
//            }
            composable(route = Screen.Activity.route) {
                ActivityScreen(viewModel = activityViewModel)
            }
            composable(route = Screen.Friends.route) {
                FriendScreen(viewModel = friendViewModel, inviteViewModel)
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen(
                    authViewModel = authViewModel,
                    goToAccountSettings = {navController.navigate(Screen.AccountSettings.route)}
                )
            }
        }
    }
}

@Composable
fun BottomNavigationBar(
    navController: NavHostController
) {
    val items = listOf(
        Screen.Home,
//        Screen.Groups,
        Screen.Activity,
        Screen.Friends,
        Screen.Profile
    )

    NavigationBar(
        modifier = Modifier.shadow(Elevation.level5),
        containerColor = MaterialTheme.colorScheme.background
    ) {
        val currentDestination = navController.currentBackStackEntryAsState().value?.destination

        items.forEach { screen ->
            val icon = when (screen) {
                Screen.Home -> R.drawable.home_icon
//                Screen.Groups -> R.drawable.group_icon
                Screen.Activity -> R.drawable.activity_icon
                Screen.Friends -> R.drawable.friends_icon
                Screen.Profile -> R.drawable.profile_icon
                else -> R.drawable.home_icon
            }

            val selected = currentDestination?.route == screen.route
            NavigationBarItem(
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(screen.route) {
                            popUpTo(Screen.Home.route)
                            launchSingleTop = true
                        }
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = MaterialTheme.colorScheme.primary,
                    selectedTextColor = MaterialTheme.colorScheme.primary,
                    unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    indicatorColor = Color.Transparent
                ),
                icon = {
                    Icon(
                        painter = painterResource(icon),
                        contentDescription = screen.route
                    )
                },
                label = {
                    Text(
                        text = screen.route.replaceFirstChar { it.uppercase() },
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            )
        }
    }
}