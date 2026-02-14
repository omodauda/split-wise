package com.example.splitwise.ui.features.main.addBill.components.stepTwo

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.splitwise.model.AddBillUiState
import com.example.splitwise.model.User
import com.example.splitwise.ui.features.main.addBill.components.stepTwo.navigation.StepTwoDestination
import com.example.splitwise.ui.features.main.addBill.components.stepTwo.navigation.StepTwoNavHost
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun StepTwo(
    uiState: AddBillUiState,
    onSelectGroup: (groupId: String) -> Unit,
    onSelectFriend: (user: User) -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()
    val currentBackStack by navController.currentBackStackEntryAsState()
    val currentDestination = currentBackStack?.destination
    val currentTab = StepTwoDestination.entries.find { it.route == currentDestination?.route } ?: StepTwoDestination.GROUPS

    StepTwoContent(
        navController = navController,
        currentTab = currentTab,
        onTabSelected = { newTab ->
            // Navigate with singleTop and launch an exclusive instance of the tab
            navController.navigate(newTab.route) {
                // Pop up to the start destination of the graph to
                // avoid building up a large stack of destinations
                // on the back stack as users select tabs.
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                // Avoid multiple copies of the same destination when
                // re-selecting the same item
                launchSingleTop = true
                // Restore state when re-selecting a previously selected item
                restoreState = true
            }
        },
        uiState,
        onSelectGroup,
        onSelectFriend,
        modifier = modifier
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepTwoContent(
    navController: NavHostController,
    currentTab: StepTwoDestination,
    onTabSelected: (StepTwoDestination) -> Unit,
   uiState: AddBillUiState,
    onSelectGroup: (groupId: String) -> Unit,
   onSelectFriend: (user: User) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = Spacing.large)
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    shape = MaterialTheme.shapes.large
                )
                .padding(Spacing.extraSmall)
        ) {
            PrimaryTabRow(
                selectedTabIndex = currentTab.ordinal,
                containerColor = Color.Transparent,
                indicator = {},
                divider = {}
            ) {
                StepTwoDestination.entries.forEach { destination ->
                    val isActive = currentTab == destination
                    Tab(
                        selected = isActive,
                        onClick = { onTabSelected(destination) },
                        text = {
                            Text(
                                text = destination.label,
                                style = MaterialTheme.typography.titleMedium,
                                color = if (isActive) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        modifier = Modifier
                            .background(
                                color = if (isActive) MaterialTheme.colorScheme.background else Color.Transparent,
                                shape = MaterialTheme.shapes.large
                            )
                    )
                }
            }
        }
        StepTwoNavHost(
            navController = navController,
            modifier = Modifier.padding(top = Spacing.medium),
            uiState = uiState,
            onSelectGroup = onSelectGroup,
            onSelectFriend = onSelectFriend
        )
    }
}


@Preview(
    name = "Light Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    name = "Dark Mode",
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun StepTwoPreview() {
    SplitWiseTheme {
        StepTwo(uiState = AddBillUiState(), onSelectGroup = {}, onSelectFriend = {})
    }
}