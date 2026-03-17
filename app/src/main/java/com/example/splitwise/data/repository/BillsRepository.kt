package com.example.splitwise.data.repository

import android.util.Log
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.example.splitwise.data.network.OwedBillsPagingSource
import com.example.splitwise.data.network.api.BillApi
import com.example.splitwise.data.network.model.ApiError
import com.example.splitwise.data.network.model.CreateBillRequest
import com.example.splitwise.data.network.model.CreateBillResponse
import com.example.splitwise.data.network.model.GetOwedBillsResponse
import com.example.splitwise.data.network.model.OwedBill
import com.google.gson.Gson
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import java.io.IOException

class BillsRepository(private val billApi: BillApi) {

    suspend fun addBill(data: CreateBillRequest): Result<CreateBillResponse> {
        return try {
            val response = billApi.addBill(data)
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

    fun getOwedBillsStream(): Flow<PagingData<OwedBill>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                OwedBillsPagingSource(billApi)
            }
        ).flow
    }

    suspend fun getOwedBillsFirstPage(): Result<List<OwedBill>> {
        return try {
            val response = billApi.getOwedBills(
                limit = 5,
                cursorId = null
            )
            val body = response.body()
            if (response.isSuccessful && body !== null) {
                Log.d("Owed", body.data.toString())
                Result.success(body.data)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        } catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}