package com.example.splitwise.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val dollarFormat = DecimalFormat("$#,##0.00", DecimalFormatSymbols(Locale.US))

fun formatAsCurrency(amount: Double): String {
    return dollarFormat.format(amount)
}