package com.example.splitwise.data.network.api

import com.example.splitwise.data.network.model.CreateBillRequest
import com.example.splitwise.data.network.model.CreateBillResponse
import com.example.splitwise.data.network.model.GetBillsDashboardResponse
import com.example.splitwise.data.network.model.GetOwedBillsResponse
import com.example.splitwise.data.network.model.GetOwingBillsResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface BillApi {
    @POST("bill/add")
    suspend fun addBill(
        @Body request: CreateBillRequest
    ): Response<CreateBillResponse>

    @GET("bill/owed")
    suspend fun getOwedBills(
        @Query("cursorId") cursorId: String?,
        @Query("limit") limit: Int?
    ): Response<GetOwedBillsResponse>

    @GET("bill/owing")
    suspend fun getOwingBills(
        @Query("cursorId") cursorId: String?,
        @Query("limit") limit: Int?
    ): Response<GetOwingBillsResponse>

    @GET("bill/dashboard")
    suspend fun getBillsDashboard(): Response<GetBillsDashboardResponse>
}