package com.example.splitwise.data.network.api

import com.example.splitwise.data.network.model.FriendInviteResponse
import com.example.splitwise.data.network.model.GetFriendsResponse
import com.example.splitwise.data.network.model.GetPendingInvitesResponse
import com.example.splitwise.data.network.model.SendFriendInviteRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface FriendApi {

    @GET("friendship")
    suspend fun getFriends(
        @Query("cursorId") cursorId: String?,
        @Query("search") search: String?
    ): Response<GetFriendsResponse>

    @POST("friendship/invite")
    suspend fun sendFriendInvite(
        @Body request: SendFriendInviteRequest
    ): Response<FriendInviteResponse>

    @GET("friendship/pending")
    suspend fun getPendingInvites(
        @Query("cursorId") cursorId: String?
    ): Response<GetPendingInvitesResponse>

    @PATCH("friendship/{inviteId}/accept")
    suspend fun acceptInvite(@Path("inviteId") inviteId: String): Response<FriendInviteResponse>

    @PATCH("friendship/{inviteId}/decline")
    suspend fun declineInvite(@Path("inviteId") inviteId: String): Response<FriendInviteResponse>

}