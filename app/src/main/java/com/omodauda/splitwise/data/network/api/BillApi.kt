package com.omodauda.splitwise.data.network.api

import com.omodauda.splitwise.data.network.model.CreateBillRequest
import com.omodauda.splitwise.data.network.model.CreateBillResponse
import com.omodauda.splitwise.data.network.model.GetBillsDashboardResponse
import com.omodauda.splitwise.data.network.model.GetOwedBillsResponse
import com.omodauda.splitwise.data.network.model.GetOwingBillsResponse
import com.omodauda.splitwise.data.network.model.PayBillRequest
import com.omodauda.splitwise.data.network.model.PayBillResponse
import com.omodauda.splitwise.data.network.model.SendBillReminderRequest
import com.omodauda.splitwise.data.network.model.SendBillReminderResponse
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

    @POST("bill/pay")
    suspend fun payBill(
        @Body request: PayBillRequest
    ): Response<PayBillResponse>

    @POST("bill/settle")
    suspend fun settleBill(
        @Body request: PayBillRequest
    ): Response<PayBillResponse>

    @POST("bill/send-reminder")
    suspend fun sendBillReminder(@Body request: SendBillReminderRequest): Response<SendBillReminderResponse>

}