package com.omodauda.splitwise.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omodauda.splitwise.data.network.api.FriendApi
import com.omodauda.splitwise.data.network.model.ApiError
import com.omodauda.splitwise.data.network.model.Friend
import com.google.gson.Gson
import okio.IOException
import retrofit2.HttpException

class FriendPagingSource(
    private val friendApi: FriendApi,
    private val searchQuery: String?
): PagingSource<String, Friend>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Friend> {
        val cursorId = params.key

        return try {
            val response = friendApi.getFriends(
                cursorId = cursorId,
                search = searchQuery
            )

            if (response.isSuccessful && response.body() !== null) {
                val getFriendsResponse = response.body()!!
                val friends = getFriendsResponse.data
                val nextCursor = getFriendsResponse.meta.nextCursor

                LoadResult.Page(
                    data = friends,
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

    override fun getRefreshKey(state: PagingState<String, Friend>): String? {
        return null
    }
}