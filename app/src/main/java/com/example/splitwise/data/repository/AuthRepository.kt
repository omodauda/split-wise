package com.example.splitwise.data.repository

import android.util.Log
import com.example.splitwise.data.local.IAuthPreference
import com.example.splitwise.data.network.api.AuthApi
import com.example.splitwise.data.network.model.ApiError
import com.example.splitwise.data.network.model.LoginRequest
import com.example.splitwise.data.network.model.LoginResponse
import com.google.gson.Gson

class AuthRepository(
    private val authPreference: IAuthPreference,
    private val authApi: AuthApi
) {
    val isAuthenticated = authPreference.isAuthenticated

    suspend fun login(data: LoginRequest): Result<LoginResponse?> {
        return try {
            val response = authApi.login(data)
            if (response.isSuccessful && response.body() !== null) {
                val loginResponse = response.body()
                // TODO: save access token
                authPreference.setAuthenticated(true)
                Result.success(loginResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Log.d("Auth", "$apiError")
                Result.failure(Exception(apiError.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout() {
        authPreference.setAuthenticated(false)
    }
}