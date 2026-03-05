package com.example.splitwise.data.network

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.splitwise.data.network.api.FriendApi
import com.example.splitwise.data.network.model.ApiError
import com.example.splitwise.data.network.model.FriendInvite
import com.google.gson.Gson
import okio.IOException
import retrofit2.HttpException

class PendingInvitesPagingSource(private val friendApi: FriendApi): PagingSource<String, FriendInvite>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, FriendInvite> {
        val cursorId = params.key

        return try {
            val response = friendApi.getPendingInvites(cursorId)
            if (response.isSuccessful && response.body() !== null) {
                val getPendingInvitesResponse = response.body()!!
                val invites = getPendingInvitesResponse.data
                val nextCursor = getPendingInvitesResponse.meta.nextCursor

                LoadResult.Page(
                    data = invites,
                    prevKey = null,
                    nextKey = nextCursor
                )
            } else {
                val errorBody = response.errorBody()?.string() ?: "Unknown error"
                val apiError = try { Gson().fromJson(errorBody, ApiError::class.java) } catch (e: Exception) { null }
                LoadResult.Error(Exception(apiError?.message))
            }
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<String, FriendInvite>): String? {
        return null
    }
}