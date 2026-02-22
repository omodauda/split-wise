package com.example.splitwise.data.repository

import com.example.splitwise.data.local.IAuthPreference
import com.example.splitwise.data.network.api.AuthApi
import com.example.splitwise.data.network.model.ApiError
import com.example.splitwise.data.network.model.AuthUserData
import com.example.splitwise.data.network.model.LoginRequest
import com.example.splitwise.data.network.model.LoginResponse
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.launch

class AuthRepository(
    private val authPreference: IAuthPreference,
    private val authApi: AuthApi
) {
    val isAuthenticated = authPreference.isAuthenticated

    private val _user = MutableStateFlow<AuthUserData?>(null)
    val user: kotlinx.coroutines.flow.StateFlow<AuthUserData?> = _user.asStateFlow()

    private val repositoryScope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    init {
        // ✨ 2. Launch a coroutine within the repository's scope ✨
        repositoryScope.launch {
            // load user from storage
            authPreference.getUser().firstOrNull()?.let { userFromStorage ->
                _user.value = userFromStorage
            }
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

                _user.value = loginResponse.data.user
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

    suspend fun logout() {
        authPreference.clearAll()
    }
}