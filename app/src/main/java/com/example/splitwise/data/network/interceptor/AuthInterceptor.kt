package com.example.splitwise.data.network.interceptor

import com.example.splitwise.data.local.AuthPreference
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val authPreference: AuthPreference): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()
        val accessToken = authPreference.getAccessTokenSync()
        if (accessToken.isNullOrBlank()) {
            return chain.proceed(originalRequest)
        }
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer $accessToken")
            .build()

        return chain.proceed(newRequest)
    }
}