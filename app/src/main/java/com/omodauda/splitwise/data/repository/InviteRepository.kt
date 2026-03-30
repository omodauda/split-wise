package com.omodauda.splitwise.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.omodauda.splitwise.data.network.PendingInvitesPagingSource
import com.omodauda.splitwise.data.network.api.FriendApi
import com.omodauda.splitwise.data.network.model.ApiError
import com.omodauda.splitwise.data.network.model.FriendInvite
import com.omodauda.splitwise.data.network.model.FriendInviteResponse
import com.omodauda.splitwise.data.network.model.SendFriendInviteRequest
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow

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

    suspend fun declineInvite(inviteId: String): Result<FriendInviteResponse> {
        return try {
            val response = friendApi.declineInvite(inviteId)
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

    suspend fun acceptInvite(inviteId: String): Result<FriendInviteResponse> {
        return try {
            val response = friendApi.acceptInvite(inviteId)
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

    fun getPendingInviteStream(): Flow<PagingData<FriendInvite>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PendingInvitesPagingSource(friendApi)
            }
        ).flow
    }
}