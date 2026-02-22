package com.example.splitwise.mock

import com.example.splitwise.data.local.IAuthPreference
import com.example.splitwise.data.network.api.AuthApi
import com.example.splitwise.data.network.model.AuthData
import com.example.splitwise.data.network.model.AuthUserData
import com.example.splitwise.data.network.model.LoginRequest
import com.example.splitwise.data.network.model.LoginResponse
import com.example.splitwise.data.repository.AuthRepository
import com.example.splitwise.di.IAppContainer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import retrofit2.Response

class FakeAuthPreferences : IAuthPreference {

    override val isAuthenticated: Flow<Boolean> = flowOf(false)

    override suspend fun setAuthenticated(value: Boolean) {
        // Do nothing — fake storage
    }

    override suspend fun saveAccessToken(token: String) {
        TODO("Not yet implemented")
    }

    override fun getAccessToken(): Flow<String?> {
        TODO("Not yet implemented")
    }

    override fun getAccessTokenSync(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(user: AuthUserData) {
        TODO("Not yet implemented")
    }

    override fun getUser(): Flow<AuthUserData?> {
        TODO("Not yet implemented")
    }

    override suspend fun clearAll() {
        TODO("Not yet implemented")
    }
}

class FakeAuthApi : AuthApi {
    // Override the login function to return a fake successful response.
    override suspend fun login(request: LoginRequest): Response<LoginResponse> {
        // For testing, we can just pretend the login is always successful
        // and return a fake token and user ID.
        val fakeResponse = LoginResponse(
            message = "Login successful",
            data = AuthData(
                token = "",
                user = AuthUserData(
                    id = "",
                    email = "",
                    fullName = "",
                    avatar = ""
                )
            )
        )
        return Response.success(fakeResponse)
        // Wrap the fake data in a successful Retrofit Response object.
//        return androidx.tracing.perfetto.handshake.protocol.Response.success(fakeResponse)
    }
}

class FakeAppContainer : IAppContainer {

    // First, implement the missing 'authApi' member.
    override val authApi: AuthApi = FakeAuthApi()

    // Now, use the fake authApi when creating the AuthRepository.
    override val authRepository = AuthRepository(
        FakeAuthPreferences(),
        authApi = this.authApi // Use the fake API instance
    )
}