package com.omodauda.splitwise

import android.app.Application
import com.omodauda.splitwise.di.IAppContainer
import com.omodauda.splitwise.di.AppContainerImpl

class SplitWiseApplication: Application() {
    lateinit var appContainer: IAppContainer

    override fun onCreate() {
        super.onCreate()
        appContainer = AppContainerImpl(this)
    }
}