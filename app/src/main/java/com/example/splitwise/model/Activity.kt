package com.example.splitwise.model

data class ActivityItem(
    val id: String,
    val title: String,
    val amount: Double,
    val payer: String,
    val paid: String
)

data class ActivitySection(
    val title: String,
    val items: List<ActivityItem>
)
