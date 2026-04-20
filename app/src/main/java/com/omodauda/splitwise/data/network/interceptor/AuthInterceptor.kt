package com.omodauda.splitwise.data.network.interceptor

import com.omodauda.splitwise.data.local.IAuthPreference
import okhttp3.Interceptor
import okhttp3.Response
import javax.inject.Inject

class AuthInterceptor @Inject constructor(private val authPreference: IAuthPreference): Interceptor {

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