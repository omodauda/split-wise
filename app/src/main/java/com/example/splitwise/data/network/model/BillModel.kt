package com.example.splitwise.data.network.model

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

data class GetOwedBillsResponse(
    val data: List<OwedBill>,
    val meta: PaginationMetaData
)
data class OwedBill(
    val id: String,
    val amount: Int,
    val user: User
)
data class User(
    val id: String,
    val fullName: String,
    val avatar: String?
)

data class GetOwingBillsResponse(
    val data: List<OwingBill>,
    val meta: PaginationMetaData
)
data class OwingBill(
    val id: String,
    val amount: Int,
    val bill: Bill
)
data class Bill(
    val id: String,
    val paidBy: User
)