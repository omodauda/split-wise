package com.example.splitwise.mock

import com.example.splitwise.data.local.IAuthPreference
import com.example.splitwise.data.network.api.AuthApi
import com.example.splitwise.data.network.api.FriendApi
import com.example.splitwise.data.network.model.AuthData
import com.example.splitwise.data.network.model.AuthUserData
import com.example.splitwise.data.network.model.FriendInviteResponse
import com.example.splitwise.data.network.model.GetFriendsResponse
import com.example.splitwise.data.network.model.GetPendingInvitesResponse
import com.example.splitwise.data.network.model.LoginRequest
import com.example.splitwise.data.network.model.LoginResponse
import com.example.splitwise.data.network.model.PaginationMetaData
import com.example.splitwise.data.network.model.SendFriendInviteRequest
import com.example.splitwise.data.network.model.SignupRequest
import com.example.splitwise.data.repository.AuthRepository
import com.example.splitwise.data.repository.FriendRepository
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
    }

    override suspend fun signup(request: SignupRequest): Response<LoginResponse> {
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
    }
}

class FakeFriendApi : FriendApi {
    override suspend fun getFriends(
        cursorId: String?,
        search: String?
    ): Response<GetFriendsResponse> {
        // Return a successful but empty response by default for tests.
        val fakeResponse = GetFriendsResponse(
            data = emptyList(),
            meta = PaginationMetaData(nextCursor = null, hasNextPage = false)
        )
        return Response.success(fakeResponse)
    }

    override suspend fun sendFriendInvite(request: SendFriendInviteRequest): Response<FriendInviteResponse> {
        // You can implement a fake response later if needed for testing invites.
        TODO("Not yet implemented")
    }

    override suspend fun acceptInvite(inviteId: String): Response<FriendInviteResponse> {
        // Return a successful response with no body (Unit).
        val fakeResponse = FriendInviteResponse(message = "Invite accepted")
        return Response.success(fakeResponse)
    }

    override suspend fun declineInvite(inviteId: String): Response<FriendInviteResponse> {
        // Return a successful response with no body (Unit).
        val fakeResponse = FriendInviteResponse(message = "Invite accepted")
        return Response.success(fakeResponse)
    }

    override suspend fun getPendingInvites(): Response<GetPendingInvitesResponse> {
        TODO("Not yet implemented")
    }
}

class FakeAppContainer : IAppContainer {

    // First, implement the missing 'authApi' member.
    override val authApi: AuthApi = FakeAuthApi()
    override val friendApi: FriendApi = FakeFriendApi()

    // Now, use the fake authApi when creating the AuthRepository.
    override val authRepository = AuthRepository(
        FakeAuthPreferences(),
        authApi = this.authApi // Use the fake API instance
    )

    override val friendRepository: FriendRepository
        get() = TODO("Not yet implemented")
}