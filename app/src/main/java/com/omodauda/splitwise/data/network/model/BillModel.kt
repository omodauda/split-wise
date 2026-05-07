package com.omodauda.splitwise.data.network.model

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
    val paidAmount: Int,
    val createdAt: Date,
    val user: User,
    val bill: OwedBillItemModel
)
data class OwedBillItemModel(
    val id: String,
    val description: String,
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
    val paidAmount: Int,
    val createdAt: Date,
    val bill: OwingBillItemModel
)
data class OwingBillItemModel(
    val id: String,
    val description: String,
    val paidBy: User
)
data class GetBillsDashboardResponse(
    val totalOwed: Int,
    val totalOwing: Int,
    val netBalance: Int
)
data class PayBillRequest(
    val splitId: String,
    val idempotencyKey: String,
    val amount: Int,
    val method: String
)
data class PayBillResponse(
    val message: String
)

data class SendBillReminderRequest(
    val splitId: String
)

data class SendBillReminderResponse(
    val message: String
)

data class GetBillDetailsRequest(
    val billId: String
)

data class BillDetails(
    val category: String,
    val date: Date,
    val description: String,
    val id: String,
    val paidBy: User,
    val splitMethod: String,
    val splits: List<Split>,
    val totalAmount: Int
)
data class Split(
    val amount: Int,
    val id: String,
    val paidAmount: Int,
    val percentage: Double,
    val user: User
)

data class GetBillDetailsResponse(
    val message: String,
    val data: BillDetails
)