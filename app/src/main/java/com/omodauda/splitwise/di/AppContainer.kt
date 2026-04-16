package com.omodauda.splitwise.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.omodauda.splitwise.BuildConfig
import com.omodauda.splitwise.data.local.AuthPreference
import com.omodauda.splitwise.data.network.api.ActivitiesApi
import com.omodauda.splitwise.data.network.api.AuthApi
import com.omodauda.splitwise.data.network.api.BillApi
import com.omodauda.splitwise.data.network.api.FriendApi
import com.omodauda.splitwise.data.network.interceptor.AuthInterceptor
import com.omodauda.splitwise.data.repository.ActivityRepository
import com.omodauda.splitwise.data.repository.AuthRepository
import com.omodauda.splitwise.data.repository.BillsRepository
import com.omodauda.splitwise.data.repository.FriendRepository
import com.omodauda.splitwise.data.repository.InviteRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


interface IAppContainer {
 val authRepository: AuthRepository
 val authApi: AuthApi
 val friendApi: FriendApi
 val friendRepository: FriendRepository
 val inviteRepository: InviteRepository
 val billApi: BillApi
 val billsRepository: BillsRepository

 val activityApi: ActivitiesApi
 val activityRepository: ActivityRepository

 val authPreference: AuthPreference
}
class AppContainerImpl(private val context: Context): IAppContainer {

    private val baseUrl = BuildConfig.BASE_URL
    override val authPreference = AuthPreference(context)

    private val gson = GsonBuilder()
        .serializeNulls()
        .create()
    private val okHttpClient: OkHttpClient by lazy {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) Level.BODY else Level.NONE
        }

        OkHttpClient.Builder()
            .connectTimeout(3, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor(authPreference))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gson))
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

    override val activityApi: ActivitiesApi by lazy {
        retrofit.create(ActivitiesApi::class.java)
    }


    override val authRepository = AuthRepository(authPreference, authApi)
    override val friendRepository = FriendRepository(friendApi)
    override val inviteRepository = InviteRepository(friendApi)
    override val billsRepository = BillsRepository(billApi)
    override val activityRepository: ActivityRepository = ActivityRepository(activityApi)
}