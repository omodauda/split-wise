package com.example.splitwise.mock

import com.example.splitwise.data.local.IAuthPreference
import com.example.splitwise.data.repository.AuthRepository
import com.example.splitwise.di.IAppContainer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeAuthPreferences : IAuthPreference {

    override val isAuthenticated: Flow<Boolean> = flowOf(false)

    override suspend fun setAuthenticated(value: Boolean) {
        // Do nothing — fake storage
    }
}

class FakeAppContainer : IAppContainer {
    override val authRepository = AuthRepository(FakeAuthPreferences())
}