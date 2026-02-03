package com.example.splitwise.ui.features.main.addBill

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppTextButton
import com.example.splitwise.ui.features.main.addBill.components.stepOne.StepOne
import com.example.splitwise.ui.theme.Elevation
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun AddBillScreen(
    goBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var currentStep by rememberSaveable { mutableStateOf(1) }

    Scaffold(
        bottomBar = {AddBillFooter()},
        modifier = modifier
            .fillMaxSize()
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(top = innerPadding.calculateTopPadding())
        ) {
            AddBillHeader(goBack, currentStep)
            StepOne()
        }
    }
}

@Composable
fun AddBillHeader(
    goBack: () -> Unit,
    currentStep: Int,
    modifier: Modifier = Modifier
) {
    var currentProgress by remember { mutableFloatStateOf(0.5f) }
    Column(
        modifier = modifier
            .padding(vertical = Spacing.extraMedium, horizontal = Spacing.large)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(Spacing.medium),
            modifier = Modifier
                .fillMaxWidth()
        ) {
            IconButton(
                onClick = {goBack()},
                modifier = Modifier
                    .background(color = MaterialTheme.colorScheme.surfaceContainer, shape = CircleShape)
            ) {
                Icon(
                    painter = painterResource(id = if(currentStep == 1) R.drawable.close_icon else R.drawable.back_icon),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onBackground
                )
            }
            Column {
                Text(
                    text = stringResource(R.string.add_bill),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${stringResource(R.string.step)} 1 ${stringResource(R.string.of)} 6",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(Modifier.height(Spacing.medium))
        LinearProgressIndicator(
            progress = { currentProgress },
            trackColor = MaterialTheme.colorScheme.surfaceContainer,
            color = MaterialTheme.colorScheme.primary,
            strokeCap = StrokeCap.Round,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
fun AddBillFooter(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .shadow(elevation = Elevation.level5)
            .background(color = MaterialTheme.colorScheme.background)
            .padding(vertical = ScreenDimensions.verticalPadding, horizontal = Spacing.large)

    ) {
        AppTextButton(
            title = stringResource(R.string.Continue),
            onClick = {}
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
fun AddBillScreenPreview() {
    SplitWiseTheme {
        AddBillScreen(goBack = {})
    }
}