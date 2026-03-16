package com.example.splitwise.data.network.model

import com.example.splitwise.model.User
import java.util.Date

data class CreateBillRequest(
    val description: String,
    val category: String,
    val date: Date,
    val payerId: String,
    val totalAmount: Int,
    val splitMethod: String,
    val splits: List<BillSplit>
)
data class BillSplit(
    val userId: String,
    val percentage: Double,
    val amount: Int
)

data class CreateBillResponse(
    val message: String
)
