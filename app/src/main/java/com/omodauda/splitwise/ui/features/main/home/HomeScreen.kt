package com.omodauda.splitwise.ui.features.main.home

import android.Manifest
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Build
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import com.google.firebase.messaging.FirebaseMessaging
import com.omodauda.splitwise.R
import com.omodauda.splitwise.data.network.model.OwedBill
import com.omodauda.splitwise.data.network.model.OwingBill
import com.omodauda.splitwise.data.network.model.SendBillReminderRequest
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenRequest
import com.omodauda.splitwise.ui.components.LoadingView
import com.omodauda.splitwise.ui.components.toast.ToastHostState
import com.omodauda.splitwise.ui.components.toast.ToastState
import com.omodauda.splitwise.ui.components.toast.ToastType
import com.omodauda.splitwise.ui.features.auth.AuthViewModel
import com.omodauda.splitwise.ui.features.main.confirmPayment.ConfirmPaymentViewModel
import com.omodauda.splitwise.ui.features.main.friends.FriendViewModel
import com.omodauda.splitwise.ui.features.main.home.components.BillSectionShimmer
import com.omodauda.splitwise.ui.features.main.home.components.DashBoard
import com.omodauda.splitwise.ui.features.main.home.components.EmptyBillView
import com.omodauda.splitwise.ui.features.main.home.components.OwedView
import com.omodauda.splitwise.ui.features.main.home.components.OwingView
import com.omodauda.splitwise.ui.features.main.home.components.PaymentPendingConfirmationAlert
import com.omodauda.splitwise.ui.features.main.home.components.PendingInvites
import com.omodauda.splitwise.ui.features.main.home.components.PendingPaymentConfirmationDialog
import com.omodauda.splitwise.ui.features.main.home.components.RecordPaymentModal
import com.omodauda.splitwise.ui.features.main.home.components.ReminderModal
import com.omodauda.splitwise.ui.features.main.home.components.SettleUpModal
import com.omodauda.splitwise.ui.features.main.invites.InviteViewModel
import com.omodauda.splitwise.ui.features.main.paymentConfirmations.PaymentPendingConfirmationViewModel
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
    authViewModel: AuthViewModel,
    toastHostState: ToastHostState,
    viewAllOwedBills: () -> Unit,
    viewAllOwingBills: () -> Unit,
    onBillItemClicked: (billId: String) -> Unit,
    goToPaymentConfirmation: () -> Unit,
    paymentPendingConfirmationViewModel: PaymentPendingConfirmationViewModel,
    confirmPaymentViewModel: ConfirmPaymentViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val invites = inviteViewModel.inviteFlow.collectAsLazyPagingItems()

    val friends = friendViewModel.friendsPagingData.collectAsLazyPagingItems()

    val billsUiState by billsViewModel.uiState.collectAsStateWithLifecycle()

    val showInviteDialog by inviteViewModel.showDialog.collectAsStateWithLifecycle()

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }

    val recordPaymentModalState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showRecordPaymentModal by rememberSaveable { mutableStateOf(false) }
    var selectedOwedBill by rememberSaveable { mutableStateOf<OwedBill?>(null) }

    val settleUpModalState = rememberModalBottomSheetState(skipPartiallyExpanded = false)
    var showSettleUpModal by rememberSaveable { mutableStateOf(false) }
    var selectedOwingBill by rememberSaveable { mutableStateOf<OwingBill?>(null) }

    var pendingPaymentAlert by rememberSaveable { mutableStateOf(true) }
    val showPendingPaymentConfirmationDialog by paymentPendingConfirmationViewModel.showDialog.collectAsStateWithLifecycle()
    val firstPendingPayment by paymentPendingConfirmationViewModel.firstPendingPayment.collectAsStateWithLifecycle()
    val pendingPaymentCount by paymentPendingConfirmationViewModel.totalCount.collectAsStateWithLifecycle()

    LaunchedEffect(firstPendingPayment) {
        firstPendingPayment?.let {
            confirmPaymentViewModel.setPaymentId(it.id)
        }
    }

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
                billsViewModel.resetSubmissionState()
            }

            is PayBillSubmissionState.Error -> {
                toastHostState.showToast(
                    ToastState(message = state.message, type = ToastType.ERROR)
                )
                billsViewModel.resetSubmissionState()
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
                    return@addOnCompleteListener
                }
                val token = task.result
                authViewModel.updateFcmToken(
                    data = UpdateFcmTokenRequest(token)
                )
            }
        } else {
//            Log.w("FCM", "Cannot fetch token: Notification permission not granted.")
        }
    }

    fun handleSendReminder() {
        val data = SendBillReminderRequest(splitId = selectedOwedBill!!.id)
        billsViewModel.sendBillReminder(data)
        showBottomSheet = false
    }


    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(start = innerPadding.calculateStartPadding(LayoutDirection.Ltr), end = innerPadding.calculateEndPadding(
                    LayoutDirection.Ltr), bottom = innerPadding.calculateBottomPadding())
                .verticalScroll(rememberScrollState())
        ) {
            DashBoard(
                paddingTop = innerPadding.calculateTopPadding(),
                onAddBill = { goToAddBill() },
                data = billsUiState.billDashboard,
                isLoading = billsUiState.dashboardLoading,
                goToPaymentPendingConfirmations = goToPaymentConfirmation,
                pendingPaymentCount = pendingPaymentCount
            )
            if (pendingPaymentAlert && pendingPaymentCount > 1) {
                PaymentPendingConfirmationAlert(
                    onReview = {goToPaymentConfirmation()},
                    onDismiss = {pendingPaymentAlert = false},
                    modifier = Modifier
                        .padding(top = 16.dp, start = ScreenDimensions.sectionSpacing, end = ScreenDimensions.sectionSpacing)
                )
            }
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
                viewAllOwedBills = viewAllOwedBills,
                viewAllOwingBills = viewAllOwingBills,
                onBillItemClicked = onBillItemClicked
            )
            if (showBottomSheet) {
                ReminderModal(
                    sheetState,
                    onDismissRequest = { showBottomSheet = false },
                    onSend = {handleSendReminder()},
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

            if (showPendingPaymentConfirmationDialog && !showInviteDialog && firstPendingPayment !== null) {
                PendingPaymentConfirmationDialog(
                    onDismiss = { paymentPendingConfirmationViewModel.onDialogDismissed() },
                    pendingPayment = firstPendingPayment!!,
                    confirmPaymentViewModel = confirmPaymentViewModel
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
    viewAllOwedBills: () -> Unit,
    viewAllOwingBills: () -> Unit,
    onBillItemClicked: (billId: String) -> Unit,
    modifier: Modifier = Modifier
) {
    val isOverAllLoading = owedLoading || owingLoading
    val isEmpty = !isOverAllLoading && owedBills.isEmpty() && owingBills.isEmpty()

    Column(
        modifier = modifier
            .padding(
                start = ScreenDimensions.sectionSpacing,
                end = ScreenDimensions.sectionSpacing,
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
                        bills = owedBills,
                        onViewAll = {viewAllOwedBills()},
                        onBillItemClicked = {onBillItemClicked(it)}
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
                    OwingView(bills = owingBills, openSettleUpModal, onViewAll = {viewAllOwingBills()}, onBillItemClicked = {onBillItemClicked(it)})
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

    SplitWiseTheme {
//        HomeScreen(
//            goToAddBill = {},
//            inviteVm,
//            friendViewModel = friendVm,
//            billsViewModel,
//            authVm,
//            toastHostState
//        )
    }
}