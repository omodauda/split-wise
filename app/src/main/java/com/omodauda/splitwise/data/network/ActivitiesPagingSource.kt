package com.omodauda.splitwise.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.omodauda.splitwise.data.network.api.ActivitiesApi
import com.omodauda.splitwise.data.network.model.Activity
import com.omodauda.splitwise.data.network.model.ApiError
import okio.IOException
import retrofit2.HttpException

class ActivitiesPagingSource(private val api: ActivitiesApi): PagingSource<String, Activity>() {

    override suspend fun load(params: LoadParams<String>): LoadResult<String, Activity> {
        val cursorId = params.key

        return try {
            val response = api.getUserActivities(
                cursorId = cursorId,
            )

            if (response.isSuccessful && response.body() !== null) {
                val getActivitiesResponse = response.body()!!
                val activities = getActivitiesResponse.data
                val nextCursor = getActivitiesResponse.meta.nextCursor

                LoadResult.Page(
                    data = activities,
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

    override fun getRefreshKey(state: PagingState<String, Activity>): String? {
        return null
    }
}