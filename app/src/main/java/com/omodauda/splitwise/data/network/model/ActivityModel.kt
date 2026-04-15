package com.omodauda.splitwise.data.network.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class GetActivitiesResponse(
    val data: List<Activity>,
    val meta: PaginationMetaData
)

enum class ActivityType {
    @SerializedName("NEW_BILL") NEW_BILL,
    @SerializedName("PAYMENT_MADE") PAYMENT_MADE,
    @SerializedName("PAYMENT_RECEIVED") PAYMENT_RECEIVED,
    @SerializedName("SPLIT_SETTLED") SPLIT_SETTLED,
    @SerializedName("FRIEND_REQUEST") FRIEND_REQUEST,
    @SerializedName("FRIEND_ACCEPTED") FRIEND_ACCEPTED,

}
data class ActivityData(
    val amount: Int?,
    val billId: String,
    val splitId: String?
)
data class ActionedBy(
    val id: String,
    val fullName: String,
    val avatar: String? = null
)
data class Activity(
    val id: String,
    val type: ActivityType,
    val title: String,
    val body: String,
    val data: ActivityData,
    val actionedBy: ActionedBy,
    val createdAt: Date
)
