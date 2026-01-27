package com.example.splitwise

import android.app.Application
import com.example.splitwise.di.IAppContainer
import com.example.splitwise.di.AppContainerImpl

class SplitWiseApplication: Application() {
    lateinit var appContainer: IAppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainerImpl(this)
    }
}