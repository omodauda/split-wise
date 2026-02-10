package com.example.splitwise.ui.features.main.home

import android.content.res.Configuration
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppIconTextButton
import com.example.splitwise.ui.features.main.home.components.DashBoard
import com.example.splitwise.ui.features.main.home.components.OwedView
import com.example.splitwise.ui.features.main.home.components.OwingView
import com.example.splitwise.ui.features.main.home.components.RecordPaymentModal
import com.example.splitwise.ui.features.main.home.components.ReminderModal
import com.example.splitwise.ui.features.main.home.components.SettleUpModal
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    goToAddBill: () -> Unit,
    modifier: Modifier = Modifier
) {
    val sheetState = rememberModalBottomSheetState()
    var showBottomSheet by remember { mutableStateOf(false) }

    val recordPaymentModalState = rememberModalBottomSheetState()
    var showRecordPaymentModal by remember { mutableStateOf(false) }

    val settleUpModalState = rememberModalBottomSheetState()
    var showSettleUpModal by remember { mutableStateOf(false) }

    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            DashBoard()
            ContentView(
                onAddBill = {goToAddBill()},
                openRecordPaymentModal = {showRecordPaymentModal = true},
                openReminderModal = {showBottomSheet = true},
                openSettleUpModal = {showSettleUpModal = true},
                modifier = Modifier
                    .weight(1f)
            )
            if (showBottomSheet) {
                ReminderModal(
                    sheetState,
                    onDismissRequest = {showBottomSheet = false}
                )
            }
            if (showRecordPaymentModal) {
                RecordPaymentModal(
                    sheetState = recordPaymentModalState,
                    onDismissRequest = {showRecordPaymentModal = false}
                )
            }
            if (showSettleUpModal) {
                SettleUpModal(
                    sheetState = settleUpModalState,
                    onDismissRequest = {showSettleUpModal = false}
                )
            }
        }
    }
}

@Composable
fun ContentView(
    onAddBill: () -> Unit,
    openReminderModal: () -> Unit,
    openRecordPaymentModal: () -> Unit,
    openSettleUpModal: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .verticalScroll(rememberScrollState())
            .padding(start = ScreenDimensions.sectionSpacing, end = ScreenDimensions.sectionSpacing, top = ScreenDimensions.verticalPadding)
    ) {
        AppIconTextButton(
            leadingIcon = R.drawable.plus_icon,
            title = stringResource(R.string.add_bill),
            onClick = {onAddBill()}
        )
        Spacer(Modifier.height(Spacing.medium))
//        EmptyBillView(
//            onAddBill,
//            modifier = Modifier
//                .weight(1f)
//        )
        OwedView(openReminderModal = {openReminderModal()}, openRecordPaymentModal = {openRecordPaymentModal()})
        Spacer(Modifier.height(Spacing.large))
        OwingView(openSettleUpModal)
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
        HomeScreen(goToAddBill = {})
    }
}