package com.omodauda.splitwise.ui.features.main.addBill

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.omodauda.splitwise.R
import com.omodauda.splitwise.ui.theme.blue_chalk
import com.omodauda.splitwise.ui.theme.crystalPeak
import com.omodauda.splitwise.ui.theme.papaya_whip

enum class AddBillSplitMethod(
    val icon: String,
    @StringRes val title: Int,
    @StringRes val description: Int,
    val color: Color
) {
    EQUAL(
        icon = "⚖️",
        title = R.string.split_equally,
        description = R.string.equal_desc,
        color = crystalPeak
    ),
    PERCENTAGE(
        icon = "📊",
        title = R.string.by_percentage,
        description = R.string.perc_desc,
        color = blue_chalk
    ),
    EXACT(
        icon = "💵",
        title = R.string.exact_amounts,
        description = R.string.exact_desc,
        color = papaya_whip
    )
}