package com.example.splitwise.model

data class SplitEntryState(
    val user: User,
    val percentage: Double = 0.00,
    val amount: Double = 0.00
)