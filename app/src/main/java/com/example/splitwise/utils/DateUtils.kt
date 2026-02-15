package com.example.splitwise.utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Formats a nullable Date into a readable string (e.g., "Oct 26, 2024").
 * Returns a default string if the date is null.
 */
fun formatDate(date: Date?): String {
    if (date == null) return "Not set"
    val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    return formatter.format(date)
}