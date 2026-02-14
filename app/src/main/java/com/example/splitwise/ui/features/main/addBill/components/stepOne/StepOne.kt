package com.example.splitwise.ui.features.main.addBill.components.stepOne

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.R
import com.example.splitwise.model.AddBillUiState
import com.example.splitwise.model.BillCategory
import com.example.splitwise.ui.components.AppDatePicker
import com.example.splitwise.ui.components.AppTextField
import com.example.splitwise.ui.theme.OnboardingTitle
import com.example.splitwise.ui.theme.ScreenDimensions
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import com.example.splitwise.ui.theme.emerald_50

@Composable
fun StepOne(
    uiState: AddBillUiState,
    onAmountChange: (String) -> Unit,
    onDescriptionChange: (String) -> Unit,
    onDateSelected: (Long?) -> Unit,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(start = Spacing.large, end = Spacing.large)
            .verticalScroll(rememberScrollState()),
    ) {
        Spacer(Modifier.height(Spacing.large))
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.primary,
                    shape = SplitWiseShapes.appIcon
                )
                .padding(Spacing.large)
        ) {
            Text(
                text = stringResource(R.string.bill_amount),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(Modifier.height(Spacing.extraSmall))
            TextField(
                value = if (uiState.billAmount == 0.0) "" else uiState.billAmount.toString(),
                onValueChange = onAmountChange,
                placeholder = {Text(text = "0.00", style = OnboardingTitle)},
                textStyle = OnboardingTitle,
                colors = TextFieldDefaults.colors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                    focusedContainerColor = MaterialTheme.colorScheme.primary,
                    unfocusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f), // Softer placeholder
                    focusedPlaceholderColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                    unfocusedTextColor = MaterialTheme.colorScheme.onPrimary
                ),
                maxLines = 1,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number
                )
            )
        }
        Spacer(Modifier.height(Spacing.large))
        AppTextField(
            label = stringResource(R.string.add_bill_desc),
            value = uiState.description,
            onValueChange = onDescriptionChange,
            leadingIcon = R.drawable.bill_icon,
            placeholder = stringResource(R.string.desc_placeholder)
        )
        Spacer(Modifier.height(Spacing.large))
        Categories(
            selectedCategory = uiState.category,
            onSelect = onCategorySelected
        )
        Spacer(Modifier.height(Spacing.large))
        AppDatePicker(
            label = stringResource(R.string.date),
            selectedDate = uiState.date?.time,
            onDateSelected = onDateSelected
        )
        Spacer(Modifier.height(Spacing.large))
    }
}

@Composable
fun Categories(
    selectedCategory: String? = null,
    onSelect: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(
        BillCategory(
            logo = "\uD83C\uDF54",
            name = R.string.food_dining
        ),
        BillCategory(
            logo = "\uD83D\uDE97",
            name = R.string.transport
        ),
        BillCategory(
            logo = "\uD83C\uDFE0",
            name = R.string.home
        ),
        BillCategory(
            logo = "\uD83C\uDFAC",
            name = R.string.entertainment
        ),
        BillCategory(
            logo = "\uD83D\uDECD\uFE0F",
            name = R.string.shopping
        ),
        BillCategory(
            logo = "\uD83D\uDCE6",
            name = R.string.other
        ),
    )
    Column(modifier) {
        Text(
            text = stringResource(R.string.category),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(Modifier.height(ScreenDimensions.itemSpacing))
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            verticalArrangement = Arrangement.spacedBy(ScreenDimensions.itemSpacing),
            modifier = Modifier
                .fillMaxWidth()

        ) {
            categories.forEach { category ->
                Category(
                    category = category,
                    selectedCategory = selectedCategory,
                    modifier = Modifier
                        .clickable(enabled = true, onClick = {onSelect(category.name.toString())})
                )
            }
        }
    }
}

@Composable
fun Category(
    selectedCategory: String? = null,
    category: BillCategory,
    modifier: Modifier = Modifier
) {
    val isSelected = selectedCategory == category.name.toString()
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .width(105.dp)
            .height(102.73.dp)
            .border(
                width = 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                shape = MaterialTheme.shapes.large
            )
            .background(
                color = if (isSelected) emerald_50 else MaterialTheme.colorScheme.background,
                shape = MaterialTheme.shapes.large
            )
    ) {
        Text(
            text = category.logo,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(Spacing.extraSmall))
        Text(
            text = stringResource(category.name),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground
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
fun StepOnePreview() {
    SplitWiseTheme {
        StepOne(
            uiState = AddBillUiState(),
            onAmountChange = {},
            onDescriptionChange = {},
            onDateSelected = {},
            onCategorySelected = {}
        )
    }
}