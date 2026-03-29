package com.example.splitwise.data.network.api

import com.example.splitwise.data.network.model.ChangePasswordRequest
import com.example.splitwise.data.network.model.ChangePasswordResponse
import com.example.splitwise.data.network.model.DeleteAccountRequest
import com.example.splitwise.data.network.model.DeleteAccountResponse
import com.example.splitwise.data.network.model.LoginRequest
import com.example.splitwise.data.network.model.LoginResponse
import com.example.splitwise.data.network.model.SignupRequest
import com.example.splitwise.data.network.model.UpdateProfileRequest
import com.example.splitwise.data.network.model.UpdateProfileResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.HTTP
import retrofit2.http.PATCH
import retrofit2.http.POST

interface AuthApi {
    @POST("auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    @POST("auth/signup")
    suspend fun signup(@Body request: SignupRequest): Response<LoginResponse>

    @PATCH("auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ChangePasswordResponse>

    // TODO: update profile
    @PATCH("auth/update-profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UpdateProfileResponse>

    // TODO: delete account
    @HTTP(method = "DELETE", path = "auth/delete-account", hasBody = true)
    suspend fun deleteAccount(@Body request: DeleteAccountRequest): Response<DeleteAccountResponse>
}