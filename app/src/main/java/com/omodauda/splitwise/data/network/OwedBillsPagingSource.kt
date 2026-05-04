package com.omodauda.splitwise.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omodauda.splitwise.data.network.api.BillApi
import com.omodauda.splitwise.data.network.model.ApiError
import com.omodauda.splitwise.data.network.model.OwedBill
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class OwedBillsPagingSource(
    private val api: BillApi,
    private val searchQuery: String?,
    private val sort: String?
): PagingSource<String, OwedBill>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, OwedBill> {
        val cursorId = params.key

        return try {
            val response = api.getOwedBills(
                cursorId = cursorId,
                limit = params.loadSize,
                search = searchQuery,
                sort = sort
            )

            if (response.isSuccessful && response.body() !== null) {
                val getOwedBillsResponse = response.body()!!
                val bills = getOwedBillsResponse.data
                val nextCursor = getOwedBillsResponse.meta.nextCursor

                LoadResult.Page(
                    data = bills,
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

    override fun getRefreshKey(state: PagingState<String, OwedBill>): String? {
        return null
    }
}