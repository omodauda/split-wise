package com.example.splitwise.model

import com.example.splitwise.data.network.model.Friend

data class SplitEntryState(
    val user: Friend,
    val percentage: Double = 0.00,
    val amount: Double = 0.00
)