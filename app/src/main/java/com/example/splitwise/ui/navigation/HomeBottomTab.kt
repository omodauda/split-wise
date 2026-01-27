package com.example.splitwise.ui.navigation

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
import com.example.splitwise.R
import com.example.splitwise.ui.features.main.activity.ActivityScreen
import com.example.splitwise.ui.features.main.friends.FriendScreen
import com.example.splitwise.ui.features.main.groups.GroupScreen
import com.example.splitwise.ui.features.main.home.HomeScreen
import com.example.splitwise.ui.features.main.profile.ProfileScreen
import com.example.splitwise.ui.theme.Elevation

@Composable
fun HomeBottomTab(
    navController: NavController,
    startDestination: String = Screen.Home.route
) {
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {BottomNavigationBar(bottomNavController)}
    ) {
        NavHost(
            navController = bottomNavController,
            startDestination = startDestination
        ) {
            composable(route = Screen.Home.route) {
                HomeScreen()
            }
            composable(route = Screen.Groups.route) {
                GroupScreen()
            }
            composable(route = Screen.Activity.route) {
                ActivityScreen()
            }
            composable(route = Screen.Friends.route) {
                FriendScreen()
            }
            composable(route = Screen.Profile.route) {
                ProfileScreen()
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
        Screen.Groups,
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
//            val isSelected = currentDestination?.route == screen.route
            val icon = when (screen) {
                Screen.Home -> R.drawable.home_icon
                Screen.Groups -> R.drawable.group_icon
                Screen.Activity -> R.drawable.activity_icon
                Screen.Friends -> R.drawable.friends_icon
                Screen.Profile -> R.drawable.profile_icon
                else -> R.drawable.home_icon
            }

            NavigationBarItem(
                selected = currentDestination?.route == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(Screen.Home.route)
                        launchSingleTop = true
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