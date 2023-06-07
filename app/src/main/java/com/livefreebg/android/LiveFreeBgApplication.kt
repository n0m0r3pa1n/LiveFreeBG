package com.livefreebg.android

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class LiveFreeBgApplication : Application() {
    override fun onCreate() {
        super.onCreate()
    }
}