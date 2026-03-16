package com.example.splitwise.ui.features.main.addBill.components.stepTwo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.compose.collectAsLazyPagingItems
import com.example.splitwise.data.network.model.Friend
import com.example.splitwise.model.AddBillUiState
import com.example.splitwise.model.User
import com.example.splitwise.ui.features.main.addBill.components.stepTwo.components.Friends
import com.example.splitwise.ui.features.main.addBill.components.stepTwo.components.Groups
import com.example.splitwise.ui.features.main.friends.FriendViewModel

@Composable
fun StepTwoNavHost(
    navController: NavHostController,
    rootNavController: NavHostController,
    startDestination: StepTwoDestination = StepTwoDestination.FRIENDS,
    uiState: AddBillUiState,
    currentUserId: String?,
    onSelectGroup: (groupId: String) -> Unit,
    onSelectFriend: (user: Friend) -> Unit,
    modifier: Modifier = Modifier
) {
    val mainNavEntry = remember(navController) {
        rootNavController.getBackStackEntry("main_graph")
    }
    val friendViewModel: FriendViewModel = viewModel(mainNavEntry)
    val friendsPagingData = friendViewModel.friendsPagingData.collectAsLazyPagingItems()
//    val searchQuery by friendViewModel.searchQuery.collectAsState()

    NavHost(
        navController,
        startDestination = startDestination.route,
        modifier
    ) {
//        composable(StepTwoDestination.GROUPS.route) {
//            Groups(
//                selectedGroupId = uiState.selectedGroupId,
//                onSelectGroup = onSelectGroup
//            )
//        }
        composable(StepTwoDestination.FRIENDS.route) {
            Friends(
                selectedFriends = uiState.selectedFriends,
                onSelectFriend = onSelectFriend,
                friends = friendsPagingData,
                currentUserId = currentUserId
            )
        }
    }
}