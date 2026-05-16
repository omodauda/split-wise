package com.omodauda.splitwise.data.network.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.util.Date

data class CreateBillRequest(
    val description: String,
    val category: String,
    val date: Date,
    val payerId: String,
    val totalAmount: Int,
    val splitMethod: String,
    val splits: List<BillSplit>,
)

@Parcelize
data class BillSplit(
    val userId: String,
    val percentage: Double,
    val amount: Int
) : Parcelable

data class CreateBillResponse(
    val message: String
)

data class GetOwedBillsResponse(
    val data: List<OwedBill>,
    val meta: PaginationMetaData
)

@Parcelize
data class OwedBill(
    val id: String,
    val amount: Int,
    val paidAmount: Int,
    val createdAt: Date,
    val user: User,
    val bill: OwedBillItemModel
) : Parcelable
@Parcelize
data class OwedBillItemModel(
    val id: String,
    val description: String,
) : Parcelable

@Parcelize
data class User(
    val id: String,
    val fullName: String,
    val avatar: String?
) : Parcelable

data class GetOwingBillsResponse(
    val data: List<OwingBill>,
    val meta: PaginationMetaData
)

@Parcelize
data class OwingBill(
    val id: String,
    val amount: Int,
    val paidAmount: Int,
    val createdAt: Date,
    val bill: OwingBillItemModel
) : Parcelable

@Parcelize
data class OwingBillItemModel(
    val id: String,
    val description: String,
    val paidBy: User
) : Parcelable

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

@Parcelize
data class BillDetails(
    val category: String,
    val date: Date,
    val description: String,
    val id: String,
    val paidBy: User,
    val splitMethod: String,
    val splits: List<Split>,
    val totalAmount: Int
) : Parcelable

@Parcelize
data class Split(
    val amount: Int,
    val id: String,
    val paidAmount: Int,
    val percentage: Double,
    val user: User,
    val settled: Boolean
) : Parcelable

data class GetBillDetailsResponse(
    val message: String,
    val data: BillDetails
)

data class GetPaymentPendingConfirmationResponse(
    val data: List<PendingPayment>,
    val totalCount: Int,
    val meta: PaginationMetaData
)

@Parcelize
data class PendingPayment(
    val amount: Int,
    val bill: Bill,
    val createdAt: Date,
    val id: String,
    val payer: User,
    val split: SplitShare
) : Parcelable

@Parcelize
data class Bill(
    val category: String,
    val description: String,
    val totalAmount: Int
) : Parcelable

@Parcelize
data class SplitShare(
    val amount: Int
) : Parcelable

data class GetPaymentDetailsResponse (
    val data: PendingPayment,
    val message: String
)

data class ConfirmPaymentResponse (
    val message: String
)
