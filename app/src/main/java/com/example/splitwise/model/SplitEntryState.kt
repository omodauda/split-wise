package com.example.splitwise.model

data class SplitEntryState(
    val user: User,
    val percentage: String = "",
    val amount: Double = 0.0
)