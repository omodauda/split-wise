package com.example.splitwise.di

import android.content.Context
import com.example.splitwise.data.local.AuthPreference
import com.example.splitwise.data.network.api.AuthApi
import com.example.splitwise.data.network.api.BillApi
import com.example.splitwise.data.network.api.FriendApi
import com.example.splitwise.data.network.interceptor.AuthInterceptor
import com.example.splitwise.data.repository.AuthRepository
import com.example.splitwise.data.repository.BillsRepository
import com.example.splitwise.data.repository.FriendRepository
import com.example.splitwise.data.repository.InviteRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory



interface IAppContainer {
 val authRepository: AuthRepository
 val authApi: AuthApi
 val friendApi: FriendApi
 val friendRepository: FriendRepository
 val inviteRepository: InviteRepository
 val billApi: BillApi
 val billsRepository: BillsRepository
}
class AppContainerImpl(private val context: Context): IAppContainer {

    private val baseUrl = "https://split-wise-backend.fly.dev/v1/"
    private val authPreference = AuthPreference(context)
    private val okHttpClient: OkHttpClient by lazy {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        OkHttpClient.Builder()
            .addInterceptor(AuthInterceptor(authPreference))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }
    override val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }
    override val friendApi: FriendApi by lazy {
        retrofit.create(FriendApi::class.java)
    }
    override val billApi: BillApi by lazy {
        retrofit.create(BillApi::class.java)
    }

    override val authRepository = AuthRepository(authPreference, authApi)
    override val friendRepository = FriendRepository(friendApi)
    override val inviteRepository = InviteRepository(friendApi)
    override val billsRepository = BillsRepository(billApi)
}