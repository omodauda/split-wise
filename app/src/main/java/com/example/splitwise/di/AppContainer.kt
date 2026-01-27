package com.example.splitwise.di

import android.content.Context
import com.example.splitwise.data.local.AuthPreference
import com.example.splitwise.data.repository.AuthRepository


interface IAppContainer {
 val authRepository: AuthRepository
}
class AppContainerImpl(private val context: Context): IAppContainer {
    private val authPreference = AuthPreference(context)

    override val authRepository = AuthRepository(authPreference)
}