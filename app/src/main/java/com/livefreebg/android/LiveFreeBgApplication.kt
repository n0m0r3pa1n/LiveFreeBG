package com.livefreebg.android

import android.app.Application
import com.facebook.appevents.AppEventsLogger
import com.livefreebg.android.data.logging.CrashlyticsTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class LiveFreeBgApplication : Application() {

    @Inject
    lateinit var crashlyticsTree: CrashlyticsTree
    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this);
        Timber.plant(crashlyticsTree)
    }
}
