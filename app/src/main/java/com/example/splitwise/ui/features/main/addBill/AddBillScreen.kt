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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.splitwise.R
import com.example.splitwise.ui.components.AppTextButton
import com.example.splitwise.ui.features.main.addBill.components.stepFive.StepFive
import com.example.splitwise.ui.features.main.addBill.components.stepFour.StepFour
import com.example.splitwise.ui.features.main.addBill.components.stepOne.StepOne
import com.example.splitwise.ui.features.main.addBill.components.stepSeven.StepSeven
import com.example.splitwise.ui.features.main.addBill.components.stepSix.StepSix
import com.example.splitwise.ui.features.main.addBill.components.stepThree.StepThree
import com.example.splitwise.ui.features.main.addBill.components.stepTwo.StepTwo
import com.example.splitwise.ui.theme.Elevation
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseTheme

@Composable
fun AddBillScreen(
    goBack: () -> Unit,
    goToAddBillSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val totalSteps = 7
    var currentStep by rememberSaveable { mutableIntStateOf(1) }

    fun goToNextStep() {
        if (currentStep < totalSteps) {
            currentStep++
            return
        }else if (currentStep == totalSteps) {
            goToAddBillSuccess()
        }
    }

    fun goToPrevStep() {
        if (currentStep > 1) {
            currentStep--
        } else {
            goBack()
        }
    }

    Scaffold(
        bottomBar = {AddBillFooter(goToNextStep = {goToNextStep()})},
        modifier = modifier
            .fillMaxSize()
    ) {innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(innerPadding)
        ) {
            AddBillHeader(currentStep, totalSteps, goToPrevStep = {goToPrevStep()})
            when (currentStep) {
                1 -> StepOne()
                2 -> StepTwo()
                3 -> StepThree()
                4 -> StepFour()
                5 -> StepFive()
                6 -> StepSix()
                7 -> StepSeven()
            }
        }
    }
}

@Composable
fun AddBillHeader(
    currentStep: Int,
    totalSteps: Int,
    goToPrevStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentProgress = currentStep.toFloat() / totalSteps.toFloat()

    val title = when (currentStep) {
        1 -> R.string.add_bill
        2 -> R.string.select_group_or_friends
        3 -> R.string.select_members
        4 -> R.string.who_paid
        5 -> R.string.split_method
        6 -> R.string.adjust_perc //& exact amounts
        7 -> R.string.review
        else -> R.string.add_bill}

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
                onClick = {goToPrevStep()},
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
                    text = stringResource(title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
                Text(
                    text = "${stringResource(R.string.step)} $currentStep ${stringResource(R.string.of)} $totalSteps",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
        Spacer(Modifier.height(Spacing.medium))
        LinearProgressIndicator(
            progress = {currentProgress},
            modifier = Modifier.fillMaxWidth().clip(shape = CircleShape),
            color = MaterialTheme.colorScheme.primary,
            trackColor = MaterialTheme.colorScheme.surfaceContainer,
            strokeCap = StrokeCap.Round,
        )
    }
}

@Composable
fun AddBillFooter(
    goToNextStep: () -> Unit,
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
            onClick = {goToNextStep()}
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
        AddBillScreen(goBack = {}, goToAddBillSuccess = {})
    }
}