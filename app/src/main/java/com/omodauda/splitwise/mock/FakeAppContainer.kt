package com.omodauda.splitwise.mock

import com.omodauda.splitwise.data.local.AuthPreference
import com.omodauda.splitwise.data.local.IAuthPreference
import com.omodauda.splitwise.data.network.api.ActivitiesApi
import com.omodauda.splitwise.data.network.api.AuthApi
import com.omodauda.splitwise.data.network.api.BillApi
import com.omodauda.splitwise.data.network.api.FriendApi
import com.omodauda.splitwise.data.network.model.AuthData
import com.omodauda.splitwise.data.network.model.AuthUserData
import com.omodauda.splitwise.data.network.model.ChangePasswordRequest
import com.omodauda.splitwise.data.network.model.ChangePasswordResponse
import com.omodauda.splitwise.data.network.model.CreateBillRequest
import com.omodauda.splitwise.data.network.model.CreateBillResponse
import com.omodauda.splitwise.data.network.model.DeleteAccountRequest
import com.omodauda.splitwise.data.network.model.DeleteAccountResponse
import com.omodauda.splitwise.data.network.model.FriendInviteResponse
import com.omodauda.splitwise.data.network.model.GetActivitiesResponse
import com.omodauda.splitwise.data.network.model.GetBillsDashboardResponse
import com.omodauda.splitwise.data.network.model.GetFriendsResponse
import com.omodauda.splitwise.data.network.model.GetOwedBillsResponse
import com.omodauda.splitwise.data.network.model.GetOwingBillsResponse
import com.omodauda.splitwise.data.network.model.GetPendingInvitesResponse
import com.omodauda.splitwise.data.network.model.LoginRequest
import com.omodauda.splitwise.data.network.model.LoginResponse
import com.omodauda.splitwise.data.network.model.PaginationMetaData
import com.omodauda.splitwise.data.network.model.PayBillRequest
import com.omodauda.splitwise.data.network.model.PayBillResponse
import com.omodauda.splitwise.data.network.model.SendBillReminderRequest
import com.omodauda.splitwise.data.network.model.SendBillReminderResponse
import com.omodauda.splitwise.data.network.model.SendFriendInviteRequest
import com.omodauda.splitwise.data.network.model.SignupRequest
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenRequest
import com.omodauda.splitwise.data.network.model.UpdateFcmTokenResponse
import com.omodauda.splitwise.data.network.model.UpdateProfileRequest
import com.omodauda.splitwise.data.network.model.UpdateProfileResponse
import com.omodauda.splitwise.data.repository.ActivityRepository
import com.omodauda.splitwise.data.repository.AuthRepository
import com.omodauda.splitwise.data.repository.BillsRepository
import com.omodauda.splitwise.data.repository.FriendRepository
import com.omodauda.splitwise.data.repository.InviteRepository
import com.omodauda.splitwise.di.IAppContainer
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

    override suspend fun changePassword(request: ChangePasswordRequest): Response<ChangePasswordResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateProfile(request: UpdateProfileRequest): Response<UpdateProfileResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAccount(request: DeleteAccountRequest): Response<DeleteAccountResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun updateFcmToken(request: UpdateFcmTokenRequest): Response<UpdateFcmTokenResponse> {
        TODO("Not yet implemented")
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

    override suspend fun getPendingInvites(cursorId: String?): Response<GetPendingInvitesResponse> {
        val fakeResponse = GetPendingInvitesResponse(
            data = emptyList(),
            meta = PaginationMetaData(nextCursor = null, hasNextPage = false)
        )
        return Response.success(fakeResponse)
    }
}

class FakeBillApi: BillApi {
    override suspend fun addBill(request: CreateBillRequest): Response<CreateBillResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getOwedBills(
        cursorId: String?,
        limit: Int?
    ): Response<GetOwedBillsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getOwingBills(
        cursorId: String?,
        limit: Int?
    ): Response<GetOwingBillsResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getBillsDashboard(): Response<GetBillsDashboardResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun payBill(request: PayBillRequest): Response<PayBillResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun settleBill(request: PayBillRequest): Response<PayBillResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun sendBillReminder(request: SendBillReminderRequest): Response<SendBillReminderResponse> {
        TODO("Not yet implemented")
    }

}

class FakeActivityApi: ActivitiesApi {
    override suspend fun getUserActivities(cursorId: String?): Response<GetActivitiesResponse> {
        TODO("Not yet implemented")
    }

}

class FakeAppContainer : IAppContainer {

    // First, implement the missing 'authApi' member.
    override val authApi: AuthApi = FakeAuthApi()
    override val friendApi: FriendApi = FakeFriendApi()
    override val authPreference: AuthPreference
        get() = TODO("Not yet implemented")

    // Now, use the fake authApi when creating the AuthRepository.
    override val authRepository = AuthRepository(
        FakeAuthPreferences(),
        authApi = this.authApi // Use the fake API instance
    )

    override val friendRepository: FriendRepository
        get() = TODO("Not yet implemented")

    override val inviteRepository: InviteRepository
        get() = TODO("Not yet implemented")

    override val billApi: BillApi = FakeBillApi()

    override val billsRepository: BillsRepository
        get() = TODO("Not yet implemented")

    override val activityApi: ActivitiesApi = FakeActivityApi()

    override val activityRepository: ActivityRepository
        get() = TODO("Not yet implemented")
}