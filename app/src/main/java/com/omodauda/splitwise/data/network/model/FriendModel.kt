package com.omodauda.splitwise.data.network.model

import java.util.Date

data class GetFriendsResponse(
    val data: List<Friend>,
    val meta: PaginationMetaData
)
data class Friend(
    val friendshipId: String,
//    val createdAt: Date,
    val userId: String,
    val fullName: String,
    val email: String,
    val avatar: String?
)
data class PaginationMetaData(
    val nextCursor: String?,
    val hasNextPage: Boolean
)
data class SendFriendInviteRequest(
    val receiverEmail: String
)
data class FriendInviteResponse(
    val message: String
)
data class GetPendingInvitesResponse(
    val data: List<FriendInvite>,
    val meta: PaginationMetaData
)
data class FriendInvite(
    val id: String,
    val createdAt: Date,
    val sender: InviteSender
)
data class InviteSender(
    val id: String,
    val fullName: String,
    val email: String,
    val avatar: String?
)