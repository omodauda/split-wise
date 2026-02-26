package com.example.splitwise.data.repository

import com.example.splitwise.data.network.api.FriendApi
import com.example.splitwise.data.network.model.ApiError
import com.example.splitwise.data.network.model.FriendInviteResponse
import com.example.splitwise.data.network.model.SendFriendInviteRequest
import com.google.gson.Gson

class InviteRepository(private val friendApi: FriendApi) {

    suspend fun sendInvite(data: SendFriendInviteRequest): Result<FriendInviteResponse> {
        return try {
            val response = friendApi.sendFriendInvite(data)
            if (response.isSuccessful && response.body() !== null) {
                val responseBody = response.body()!!
                Result.success(responseBody)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}