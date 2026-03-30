package com.omodauda.splitwise.ui.features.main.home

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.firebase.messaging.FirebaseMessaging
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.OwedBill
import com.omodauda.splitwise.data.network.model.OwingBill
import com.omodauda.splitwise.mock.FakeAppContainer
import com.omodauda.splitwise.ui.components.LoadingView
import com.omodauda.splitwise.ui.components.toast.ToastHostState
import com.omodauda.splitwise.ui.components.toast.ToastState
import com.omodauda.splitwise.ui.components.toast.ToastType
import com.omodauda.splitwise.ui.components.toast.rememberToastHostState
import com.omodauda.splitwise.ui.features.main.friends.FriendViewModel
import com.omodauda.splitwise.ui.features.main.home.components.BillSectionShimmer
import com.omodauda.splitwise.ui.features.main.home.components.DashBoard
import com.omodauda.splitwise.ui.features.main.home.components.EmptyBillView
import com.omodauda.splitwise.ui.features.main.home.components.OwedView
import com.omodauda.splitwise.ui.features.main.home.components.OwingView
import com.omodauda.splitwise.ui.features.main.home.components.PendingInvites
import com.omodauda.splitwise.ui.features.main.home.components.RecordPaymentModal
import com.omodauda.splitwise.ui.features.main.home.components.ReminderModal
import com.omodauda.splitwise.ui.features.main.home.components.SettleUpModal
import com.omodauda.splitwise.ui.features.main.invites.InviteViewModel
import com.omodauda.splitwise.ui.theme.ScreenDimensions
import com.omodauda.splitwise.ui.theme.Spacing
import com.omodauda.splitwise.ui.theme.SplitWiseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToAddBill: () -> Unit,
    inviteViewModel: InviteViewModel,
    friendViewModel: FriendViewModel,
    billsViewModel: BillsViewModel,
    toastHostState: ToastHostState,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val invites = inviteViewModel.inviteFlow.collectAsLazyPagingItems()

    val friends = friendViewModel.friendsPagingData.collectAsLazyPagingItems()

    val billsUiState by billsViewModel.uiState.collectAsStateWithLifecycle()

    val showInviteDialog by inviteViewModel.showDialog.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember { mutableStateOf(false) }

    val recordPaymentModalState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showRecordPaymentModal by remember { mutableStateOf(false) }
    var selectedOwedBill by remember { mutableStateOf<OwedBill?>(null) }

    val settleUpModalState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showSettleUpModal by remember { mutableStateOf(false) }
    var selectedOwingBill by remember { mutableStateOf<OwingBill?>(null) }

    LaunchedEffect(invites) {
        inviteViewModel.monitorLoadState(invites)
    }

    LaunchedEffect(billsUiState.billActionState) {
        when (val state = billsUiState.billActionState) {
            is PayBillSubmissionState.Loading -> {
                // LoadingView is handled in the UI body below
            }

            is PayBillSubmissionState.Success -> {
                showSettleUpModal = false
                selectedOwingBill = null
                showRecordPaymentModal = false
                selectedOwedBill = null
                toastHostState.showToast(
                    ToastState(message = state.message, type = ToastType.SUCCESS)
                )
            }

            is PayBillSubmissionState.Error -> {
                toastHostState.showToast(
                    ToastState(message = state.message, type = ToastType.ERROR)
                )
            }

            else -> {}
        }
    }

    LaunchedEffect(Unit) {
        val hasPermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.POST_NOTIFICATIONS
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            true
        }
        if (hasPermission) {
            FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w("FCM", "Fetching FCM registration token failed", task.exception)
                    return@addOnCompleteListener
                }
                val token = task.result
                // TODO: send token to backend
            }
        } else {
//            Log.w("FCM", "Cannot fetch token: Notification permission not granted.")
        }
    }


    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DashBoard(
                paddingTop = innerPadding.calculateTopPadding(),
                onAddBill = { goToAddBill() },
                data = billsUiState.billDashboard,
                isLoading = billsUiState.dashboardLoading
            )
            ContentView(
                owedLoading = billsUiState.isOwedBillsLoading,
                owedBills = billsUiState.owedBills,
                owingLoading = billsUiState.isOwingBillsLoading,
                owingBills = billsUiState.owingBills,
                openRecordPaymentModal = { bill ->
                    selectedOwedBill = bill
                    showRecordPaymentModal = true }
                ,
                openReminderModal = { bill ->
                    selectedOwedBill = bill
                    showBottomSheet = true
                },
                openSettleUpModal = { bill ->
                    selectedOwingBill = bill
                    showSettleUpModal = true
                },
                modifier = Modifier
                    .weight(1f)
            )
            if (showBottomSheet) {
                ReminderModal(
                    sheetState,
                    onDismissRequest = { showBottomSheet = false },
                    bill = selectedOwedBill
                )
            }
            if (showRecordPaymentModal) {
                RecordPaymentModal(
                    sheetState = recordPaymentModalState,
                    onDismissRequest = {
                        showRecordPaymentModal = false
                        selectedOwedBill = null
                    },
                    bill = selectedOwedBill,
                    settleBill = {data -> billsViewModel.settleBill(data)}
                )
            }
            if (showSettleUpModal) {
                SettleUpModal(
                    bill = selectedOwingBill,
                    sheetState = settleUpModalState,
                    onDismissRequest = {
                        showSettleUpModal = false
                        selectedOwingBill = null
                    },
                    payBill = { data -> billsViewModel.payBill(data) }
                )
            }

            if (billsUiState.billActionState is PayBillSubmissionState.Loading) {
                LoadingView()
            }

            if (showInviteDialog) {
                PendingInvites(
                    dismiss = { inviteViewModel.onDialogDismissed() },
                    invites = invites,
                    onAccept = { id ->
                        inviteViewModel.acceptInvite(id) {
                            invites.refresh()
                            friends.refresh()
                        }
                    },
                    onDecline = {
                        inviteViewModel.declineInvite(it) {
                            invites.refresh()
                        }
                    },
                )
            }
        }
    }
}

@Composable
fun ContentView(
    owedLoading: Boolean,
    owedBills: List<OwedBill>,
    owingLoading: Boolean,
    owingBills: List<OwingBill>,
    openReminderModal: (OwedBill) -> Unit,
    openRecordPaymentModal: (OwedBill) -> Unit,
    openSettleUpModal: (OwingBill) -> Unit,
    modifier: Modifier = Modifier
) {
    val isOverAllLoading = owedLoading || owingLoading
    val isEmpty = !isOverAllLoading && owedBills.isEmpty() && owingBills.isEmpty()

    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(
                start = ScreenDimensions.sectionSpacing,
                end = ScreenDimensions.sectionSpacing,
                top = ScreenDimensions.verticalPadding
            )
    ) {
        Spacer(Modifier.height(Spacing.medium))
        when {
            isEmpty -> {
                EmptyBillView(
                    modifier = Modifier
                        .weight(1f)
                )
            }

            else -> {
                if (owedLoading) {
                    BillSectionShimmer(
                        titleRes = R.string.you_are_owed,
                        iconRes = R.drawable.arrow_down,
                        itemCount = 5,
                    )
                } else if (owedBills.isNotEmpty()) {
                    OwedView(
                        openReminderModal = openReminderModal,
                        openRecordPaymentModal = openRecordPaymentModal,
                        bills = owedBills
                    )
                }
                Spacer(Modifier.height(Spacing.large))
                if (owingLoading) {
                    BillSectionShimmer(
                        titleRes = R.string.you_owe,
                        iconRes = R.drawable.arrow_up,
                        itemCount = 5,
                    )
                } else if (owingBills.isNotEmpty()) {
                    OwingView(bills = owingBills, openSettleUpModal)
                }
            }
        }
        Spacer(Modifier.height(Spacing.large))
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
fun HomeScreenPreview() {
    val container = FakeAppContainer()
    val inviteVm = InviteViewModel(container.inviteRepository)
    val friendVm = FriendViewModel(container.friendRepository)
    val billsViewModel = BillsViewModel(container.billsRepository)
    val toastHostState = rememberToastHostState()
    SplitWiseTheme {
        HomeScreen(
            goToAddBill = {},
            inviteVm,
            friendViewModel = friendVm,
            billsViewModel,
            toastHostState
        )
    }
}