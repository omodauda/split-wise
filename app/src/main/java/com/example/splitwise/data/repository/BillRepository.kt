package com.example.splitwise.data.repository

import android.util.Log
import com.example.splitwise.data.network.api.BillApi
import com.example.splitwise.data.network.model.ApiError
import com.example.splitwise.data.network.model.CreateBillRequest
import com.example.splitwise.data.network.model.CreateBillResponse
import com.google.gson.Gson

class BillRepository(private val billApi: BillApi) {

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
}