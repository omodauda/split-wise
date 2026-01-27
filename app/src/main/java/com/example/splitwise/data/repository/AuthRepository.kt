package com.example.splitwise.data.repository

import com.example.splitwise.data.local.IAuthPreference

class AuthRepository(private val authPreference: IAuthPreference) {
    val isAuthenticated = authPreference.isAuthenticated

    suspend fun authenticate() {
        authPreference.setAuthenticated(true)
    }

    suspend fun logout() {
        authPreference.setAuthenticated(false)
    }
}