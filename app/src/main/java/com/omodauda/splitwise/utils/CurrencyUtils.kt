package com.omodauda.splitwise.utils

import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

private val dollarFormat = DecimalFormat("$#,##0.00", DecimalFormatSymbols(Locale.US))

private val plainNumberFormat = DecimalFormat("$#,##0.00", DecimalFormatSymbols(Locale.US))
fun formatAsCurrency(amount: Double): String {
    return dollarFormat.format(amount)
}

fun formatFromCents(cents: Long): String {
    val dollars = cents / 100.0
    return plainNumberFormat.format(dollars)
}
/**
 * Overload for Int if your backend/model uses Int for cents
 */
fun formatFromCents(cents: Int): String {
    return formatFromCents(cents.toLong())
}