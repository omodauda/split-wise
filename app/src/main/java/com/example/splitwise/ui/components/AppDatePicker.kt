package com.example.splitwise.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.splitwise.ui.theme.Spacing
import com.example.splitwise.ui.theme.SplitWiseShapes
import com.example.splitwise.ui.theme.SplitWiseTheme
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// Utility function to format the date from milliseconds to a readable string
fun convertMillisToDateString(millis: Long?): String {
    if (millis == null) return ""
    val formatter = SimpleDateFormat("dd-MM-YYYY", Locale.getDefault())
    return formatter.format(Date(millis))
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDatePicker(
    modifier: Modifier = Modifier,
    label: String? = null,
    selectedDate: Long?,
    onDateSelected: (Long?) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (!label.isNullOrEmpty()) {
            Text(
                text = "Date",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Spacer(Modifier.height(Spacing.small))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(59.dp)
                .clickable(
                    onClick = { showDatePicker = true },
                    // This makes the clickable area behave as if it's for the TextField
                    // which improves accessibility.
                    role = androidx.compose.ui.semantics.Role.Button
                )
        ) {
            OutlinedTextField(
                value = convertMillisToDateString(selectedDate),
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .fillMaxSize(),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Select date"
                    )
                },
                // Disable interaction with the text field itself, the parent Box handles clicks.
                enabled = false,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
                    focusedBorderColor = MaterialTheme.colorScheme.outlineVariant,
//                    unfocusedContainerColor = MaterialTheme.colorScheme.background,
//                    focusedContainerColor = MaterialTheme.colorScheme.background,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                ),
                shape = SplitWiseShapes.inputField
            )
        }
        if (showDatePicker) {
            val datePickerState = rememberDatePickerState(
                initialSelectedDateMillis = selectedDate
            )
            DatePickerDialog(
                onDismissRequest = { showDatePicker = false },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onDateSelected(datePickerState.selectedDateMillis)
                            showDatePicker = false
                        }
                    ) { Text("OK") }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            showDatePicker = false
                        }
                    ) { Text("Cancel") }
                }
            ) {
                DatePicker(state = datePickerState)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun DatePickerPreview() {
    SplitWiseTheme {
        var date by remember { mutableStateOf<Long?>(null) }
        AppDatePicker(
            label = "Date",
            selectedDate = date,
            onDateSelected = { newDate ->
                date = newDate
            }
        )
    }
}
