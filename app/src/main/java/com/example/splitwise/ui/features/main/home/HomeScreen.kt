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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppIconTextButton
import com.example.splitwise.ui.features.main.home.components.DashBoard
import com.example.splitwise.ui.features.main.home.components.OwedView
import com.example.splitwise.ui.features.main.home.components.OwingView
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier
            .fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            DashBoard()
            ContentView(
                onAddBill = {},
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .weight(1f)
            )
        }
    }
}

@Composable
fun ContentView(
    onAddBill: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
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
        OwedView()
        Spacer(Modifier.height(Spacing.large))
        OwingView()
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
        HomeScreen()
    }
}