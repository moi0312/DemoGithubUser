package com.edlo.mydemoapp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class MyDemoApp: Application() {
    companion object {
        lateinit var INSTANCE: MyDemoApp
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
    }
}