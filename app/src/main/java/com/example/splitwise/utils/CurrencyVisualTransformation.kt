package com.example.splitwise.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class CurrencyAmountInputVisualTransformation() : VisualTransformation {

//    private val numberOfDecimals = 2
    private val dollarFormat = DecimalFormat("$#,##0.00", DecimalFormatSymbols(Locale.US))

    override fun filter(text: AnnotatedString): TransformedText {
//        val symbols = DecimalFormatSymbols.getInstance()
        val inputString = text.text.filter { it.isDigit() }

        val intValue = inputString.toLongOrNull() ?: 0L
//        val lendedString = intValue.toString().padStart(numberOfDecimals + 1, '0')

//        val integerPart = lendedString.dropLast(numberOfDecimals)
//        val decimalPart = lendedString.takeLast(numberOfDecimals)

        // Format with commas and currency symbol
        val formattedNumber = dollarFormat.format(
            intValue.toDouble() / 100
        )

        val newText = AnnotatedString(
            text = formattedNumber,
            spanStyles = text.spanStyles
        )

        return TransformedText(newText, CurrencyOffsetMapping(text.text, formattedNumber))
    }
}

class CurrencyOffsetMapping(
    private val originalText: String,
    private val transformedText: String
) : OffsetMapping {
    override fun originalToTransformed(offset: Int): Int = transformedText.length
    override fun transformedToOriginal(offset: Int): Int = originalText.length
}