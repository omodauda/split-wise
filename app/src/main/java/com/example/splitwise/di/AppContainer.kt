package com.example.splitwise.di

import android.content.Context
import com.example.splitwise.data.local.AuthPreference
import com.example.splitwise.data.network.api.AuthApi
import com.example.splitwise.data.repository.AuthRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



interface IAppContainer {
 val authRepository: AuthRepository
 val authApi: AuthApi
}
class AppContainerImpl(private val context: Context): IAppContainer {

    private val baseUrl = "https://split-wise-backend.fly.dev/v1/"
    private val authPreference = AuthPreference(context)

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    override val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    override val authRepository = AuthRepository(authPreference, authApi)
}