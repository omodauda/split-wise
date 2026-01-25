package com.example.splitwise.model

import androidx.annotation.ArrayRes
import androidx.annotation.DrawableRes

data class OnboardingData(
    @DrawableRes val image: Int,
    @ArrayRes val titles: Int,
    @ArrayRes val title2: Int,
    @ArrayRes val description: Int
)
