package com.omodauda.splitwise.data.repository

import com.omodauda.splitwise.data.local.IAuthPreference
import com.omodauda.splitwise.data.network.api.AuthApi
import com.omodauda.splitwise.data.network.model.ApiError
import com.omodauda.splitwise.data.network.model.AuthUserData
import com.omodauda.splitwise.data.network.model.ChangePasswordRequest
import com.omodauda.splitwise.data.network.model.ChangePasswordResponse
import com.omodauda.splitwise.data.network.model.DeleteAccountRequest
import com.omodauda.splitwise.data.network.model.DeleteAccountResponse
import com.omodauda.splitwise.data.network.model.LoginRequest
import com.omodauda.splitwise.data.network.model.LoginResponse
import com.omodauda.splitwise.data.network.model.SignupRequest
import com.omodauda.splitwise.data.network.model.UpdateProfileRequest
import com.omodauda.splitwise.data.network.model.UpdateProfileResponse
import com.google.gson.Gson
import com.omodauda.splitwise.data.network.model.GoogleAuthRequest
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenRequest
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenResponse
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val authPreference: IAuthPreference,
    private val authApi: AuthApi
) {
    val isAuthenticated = authPreference.isAuthenticated
    val user: Flow<AuthUserData?> = authPreference.getUser()

    suspend fun signup(data: SignupRequest): Result<LoginResponse> {
        return try {
            val response = authApi.signup(data)
            if (response.isSuccessful && response.body() !== null) {
                val signupResponse = response.body()!!
                // save access token
                authPreference.saveAccessToken(signupResponse.data.token)
                // save user data
                authPreference.saveUser(signupResponse.data.user)
                // authenticate user
                authPreference.setAuthenticated(true)
                Result.success(signupResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun googleAuth(data: GoogleAuthRequest): Result<LoginResponse> {
        return try {
            val response = authApi.googleAuth(data)
            if (response.isSuccessful && response.body() !== null) {
                val authResponse = response.body()!!
                // save access token
                authPreference.saveAccessToken(authResponse.data.token)
                // save user data
                authPreference.saveUser(authResponse.data.user)
                // authenticate user
                authPreference.setAuthenticated(true)
                Result.success(authResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    suspend fun login(data: LoginRequest): Result<LoginResponse?> {
        return try {
            val response = authApi.login(data)
            if (response.isSuccessful && response.body() !== null) {
                val loginResponse = response.body()!!
                // save access token
                authPreference.saveAccessToken(loginResponse.data.token)
                // save user data
                authPreference.saveUser(loginResponse.data.user)
                // authenticate user
                authPreference.setAuthenticated(true)
                Result.success(loginResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun changePassword(data: ChangePasswordRequest): Result<ChangePasswordResponse> {
        return try {
            val response = authApi.changePassword(data)
            if (response.isSuccessful && response.body() !== null) {
                val changePasswordResponse = response.body()!!
                Result.success(changePasswordResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateProfile(data: UpdateProfileRequest): Result<UpdateProfileResponse> {
        return try {
            val response = authApi.updateProfile(data)
            if (response.isSuccessful && response.body() !== null) {
                val updateProfileResponse = response.body()!!
                authPreference.saveUser(updateProfileResponse.data)
                Result.success(updateProfileResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteAccount(data: DeleteAccountRequest): Result<DeleteAccountResponse> {
        return try {
            val response = authApi.deleteAccount(data)
            if (response.isSuccessful && response.body() !== null) {
                val deleteAccountResponse = response.body()!!
                Result.success(deleteAccountResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateFcmToken(data: UpdateFcmTokenRequest): Result<UpdateFcmTokenResponse> {
        return try {
            val response = authApi.updateFcmToken(data)
            if (response.isSuccessful && response.body() !== null) {
                val updateTokenResponse = response.body()!!
                Result.success(updateTokenResponse)
            } else {
                val errorBody = response.errorBody()?.string()
                val apiError = Gson().fromJson(errorBody, ApiError::class.java)
                Result.failure(Exception(apiError.message))
            }

        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun logout(scope: kotlinx.coroutines.CoroutineScope) {
        scope.launch {
            updateFcmToken(UpdateFcmTokenRequest(null))
        }
        authPreference.clearAll()
    }
}