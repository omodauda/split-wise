package com.omodauda.splitwise.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.google.gson.Gson
import com.omodauda.splitwise.data.network.api.BillApi
import com.omodauda.splitwise.data.network.model.ApiError
import com.omodauda.splitwise.data.network.model.PendingPayment
import okio.IOException
import retrofit2.HttpException

class PendingPaymentPagingSource(
    private val api: BillApi,
    private val searchQuery: String?,
    private val sort: String?
): PagingSource<String, PendingPayment>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, PendingPayment> {
        val cursorId = params.key

        return try {
            val response = api.getPaymentPendingConfirmation(
                cursorId,
                limit = params.loadSize,
                search = searchQuery,
                sort = sort
            )
            if (response.isSuccessful && response.body() !== null) {
                val getPendingPaymentsResponse = response.body()!!
                val payments = getPendingPaymentsResponse.data
                val nextCursor = getPendingPaymentsResponse.meta.nextCursor

                LoadResult.Page(
                    data = payments,
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

    override fun getRefreshKey(state: PagingState<String, PendingPayment>): String? {
        return null
    }
}