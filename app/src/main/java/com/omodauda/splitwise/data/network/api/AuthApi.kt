package com.omodauda.splitwise.data.network.api

import com.omodauda.splitwise.data.network.model.ChangePasswordRequest
import com.omodauda.splitwise.data.network.model.ChangePasswordResponse
import com.omodauda.splitwise.data.network.model.DeleteAccountRequest
import com.omodauda.splitwise.data.network.model.DeleteAccountResponse
import com.omodauda.splitwise.data.network.model.LoginRequest
import com.omodauda.splitwise.data.network.model.LoginResponse
import com.omodauda.splitwise.data.network.model.SignupRequest
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenRequest
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenResponse
import com.omodauda.splitwise.data.network.model.UpdateProfileRequest
import com.omodauda.splitwise.data.network.model.UpdateProfileResponse
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

    @PATCH("auth/update-profile")
    suspend fun updateProfile(@Body request: UpdateProfileRequest): Response<UpdateProfileResponse>

    @HTTP(method = "DELETE", path = "auth/delete-account", hasBody = true)
    suspend fun deleteAccount(@Body request: DeleteAccountRequest): Response<DeleteAccountResponse>

    @PATCH("auth/update-fcm-token")
    suspend fun updateFcmToken(@Body request: UpdateFcmTokenRequest): Response<UpdateFcmTokenResponse>
}