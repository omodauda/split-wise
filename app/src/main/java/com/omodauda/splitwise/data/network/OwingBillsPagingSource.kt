package com.omodauda.splitwise.data.network

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.omodauda.splitwise.data.network.api.BillApi
import com.omodauda.splitwise.data.network.model.ApiError
import com.omodauda.splitwise.data.network.model.OwingBill
import com.google.gson.Gson
import retrofit2.HttpException
import java.io.IOException

class OwingBillsPagingSource(private val api: BillApi): PagingSource<String, OwingBill>() {
    override suspend fun load(params: LoadParams<String>): LoadResult<String, OwingBill> {
        val cursorId = params.key

        return try {
            val response = api.getOwingBills(
                cursorId = cursorId,
                limit = params.loadSize
            )

            if (response.isSuccessful && response.body() !== null) {
                val getOwingBillsResponse = response.body()!!
                val bills = getOwingBillsResponse.data
                val nextCursor = getOwingBillsResponse.meta.nextCursor

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

    override fun getRefreshKey(state: PagingState<String, OwingBill>): String? {
        return null
    }
}