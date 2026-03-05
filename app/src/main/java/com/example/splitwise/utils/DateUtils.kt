package com.example.splitwise.utils

import java.text.SimpleDateFormat
import java.time.Duration
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
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

fun formatRelativeTime(date: Date?): String {
    if (date == null) return ""

    return try {
        val pastInstant = date.toInstant()
        val now = java.time.Instant.now()
        val duration = Duration.between(pastInstant, now)

        val seconds = duration.seconds
        val minutes = duration.toMinutes()
        val hours = duration.toHours()
        val days = duration.toDays()

        when {
            seconds < 60 -> "Just now"
            minutes < 60 -> "$minutes min${if (minutes > 1) "s" else ""} ago"
            hours < 24 -> "$hours hour${if (hours > 1) "s" else ""} ago"
            days < 7 -> "$days day${if (days > 1) "s" else ""} ago"
            else -> {
                // Fallback to your existing formatDate logic for very old dates
                val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                    .withZone(ZoneId.systemDefault())
                formatter.format(pastInstant)
            }
        }
    } catch (e: DateTimeParseException) {
        "Invalid date"
    }
}