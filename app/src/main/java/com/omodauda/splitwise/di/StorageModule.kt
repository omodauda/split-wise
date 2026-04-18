package com.omodauda.splitwise.di

import android.content.Context
import com.omodauda.splitwise.data.local.AuthPreference
import com.omodauda.splitwise.data.local.IAuthPreference
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object StorageModule {

    @Provides
    @Singleton
    fun provideAuthPreference(@ApplicationContext context: Context): IAuthPreference {
        return AuthPreference(context)
    }
}