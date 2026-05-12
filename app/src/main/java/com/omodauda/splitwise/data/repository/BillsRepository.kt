package com.omodauda.splitwise.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.gson.Gson
import com.omodauda.splitwise.data.network.OwedBillsPagingSource
import com.omodauda.splitwise.data.network.OwingBillsPagingSource
import com.omodauda.splitwise.data.network.PendingPaymentPagingSource
import com.omodauda.splitwise.data.network.api.BillApi
import com.omodauda.splitwise.data.network.model.ApiError
import com.omodauda.splitwise.data.network.model.BillDetails
import com.omodauda.splitwise.data.network.model.ConfirmPaymentResponse
import com.omodauda.splitwise.data.network.model.CreateBillRequest
import com.omodauda.splitwise.data.network.model.CreateBillResponse
import com.omodauda.splitwise.data.network.model.GetBillDetailsRequest
import com.omodauda.splitwise.data.network.model.GetBillsDashboardResponse
import com.omodauda.splitwise.data.network.model.GetPaymentDetailsResponse
import com.omodauda.splitwise.data.network.model.GetPaymentPendingConfirmationResponse
import com.omodauda.splitwise.data.network.model.OwedBill
import com.omodauda.splitwise.data.network.model.OwingBill
import com.omodauda.splitwise.data.network.model.PayBillRequest
import com.omodauda.splitwise.data.network.model.PendingPayment
import com.omodauda.splitwise.data.network.model.SendBillReminderRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BillsRepository @Inject constructor(private val billApi: BillApi) {
    private val _refreshOwedBillsSignal = MutableSharedFlow<Unit>(replay = 0)
    val refreshOwedBillSignal = _refreshOwedBillsSignal.asSharedFlow()

    private val _refreshOwingBillsSignal = MutableSharedFlow<Unit>(replay = 0)
    val refreshOwingBillSignal = _refreshOwingBillsSignal.asSharedFlow()

    private val _paymentPendingConfirmationTrigger = MutableSharedFlow<Unit>(replay = 0)
    val paymentPendingConfirmationSignal = _paymentPendingConfirmationTrigger.asSharedFlow()

    suspend fun triggerRefreshOwedBills() {
        _refreshOwedBillsSignal.emit(Unit)
    }

    suspend fun triggerRefreshOwingBills() {
        _refreshOwingBillsSignal.emit(Unit)
    }
    suspend fun triggerRefreshPaymentPendingConfirmation() {
        _paymentPendingConfirmationTrigger.emit(Unit)
    }

    suspend fun addBill(data: CreateBillRequest): Result<CreateBillResponse> {
        return try {
            val response = billApi.addBill(data)
            if (response.isSuccessful && response.body() !== null) {
                triggerRefreshOwingBills()
                triggerRefreshOwedBills()
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

    fun getOwedBillsStream(searchQuery: String?, sort: String?): Flow<PagingData<OwedBill>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                OwedBillsPagingSource(billApi, searchQuery, sort)
            }
        ).flow
    }

    suspend fun getOwedBillsFirstPage(): Result<List<OwedBill>> {
        return try {
            val response = billApi.getOwedBills(
                limit = 5,
                cursorId = null,
                search = null,
                sort = null
            )
            val body = response.body()
            if (response.isSuccessful && body !== null) {
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

    fun getOwingBillsStream(searchQuery: String?, sort: String?): Flow<PagingData<OwingBill>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                OwingBillsPagingSource(billApi, searchQuery, sort)
            }
        ).flow
    }

    suspend fun getOwingBillsFirstPage(): Result<List<OwingBill>> {
        return try {
            val response = billApi.getOwingBills(
                limit = 5,
                cursorId = null,
                search = null,
                sort = null
            )
            val body = response.body()
            if (response.isSuccessful && body !== null) {
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

    suspend fun getBillsDashboard(): Result<GetBillsDashboardResponse> {
        return try {
            val response = billApi.getBillsDashboard()
            val body = response.body()
//            Log.d("dashboard", body.toString())
            if (response.isSuccessful && body !== null) {
                Result.success(body)
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

    suspend fun payBill(data: PayBillRequest): Result<String> {
        return try {
            val response = billApi.payBill(data)
            val body = response.body()
            if (response.isSuccessful && body !== null) {
                triggerRefreshOwingBills()
                Result.success(body.message)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        }catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun settleBill(data: PayBillRequest): Result<String> {
        return try {
            val response = billApi.settleBill(data)
            val body = response.body()
            if (response.isSuccessful && body !== null) {
                triggerRefreshOwedBills()
                Result.success(body.message)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        }catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun sendBillReminder(data: SendBillReminderRequest): Result<String> {
        return try {
            val response = billApi.sendBillReminder(data)
            val body = response.body()
            if (response.isSuccessful && body !== null) {
                Result.success(body.message)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        }catch (e: IOException) {
            Result.failure(e)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getBillDetails(data: GetBillDetailsRequest): Result<BillDetails> {
        return try {
            val response = billApi.getBillDetails(data.billId)
            val body = response.body()
            if (response.isSuccessful && body !== null) {
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

    fun getPaymentPendingConfirmationStream(searchQuery: String?, sort: String?): Flow<PagingData<PendingPayment>> {
        return Pager(
            config = PagingConfig(
                pageSize = 10,
                enablePlaceholders = false
            ),
            pagingSourceFactory = {
                PendingPaymentPagingSource(billApi, searchQuery, sort)
            }
        ).flow
    }

    suspend fun getPaymentPendingConfirmationFirstPage(): Result<GetPaymentPendingConfirmationResponse> {
        return try {
            val response = billApi.getPaymentPendingConfirmation(
                limit = 5, // Or whatever initial amount you need
                cursorId = null,
                search = null,
                sort = null
            )
            val body = response.body()
            if (response.isSuccessful && body !== null) {
                Result.success(body)
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

    suspend fun getPaymentDetails(paymentId: String): Result<GetPaymentDetailsResponse> {
        return try {
            val response = billApi.getPaymentDetails(paymentId)
            val body = response.body()
            if (response.isSuccessful && body !== null) {
                Result.success(body)
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

    suspend fun confirmPayment(paymentId: String): Result<ConfirmPaymentResponse> {
        return try {
            val response = billApi.confirmPayment(paymentId)
            val body = response.body()
            if (response.isSuccessful && body !== null) {
                Result.success(body)
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