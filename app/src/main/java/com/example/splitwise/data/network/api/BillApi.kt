package com.example.splitwise.data.network.api

import com.example.splitwise.data.network.model.CreateBillRequest
import com.example.splitwise.data.network.model.CreateBillResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface BillApi {

    @POST("bill/add")
    suspend fun addBill(
        @Body request: CreateBillRequest
    ): Response<CreateBillResponse>

}