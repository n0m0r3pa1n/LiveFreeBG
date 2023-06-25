package com.livefreebg.android

import android.app.Application
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import com.facebook.appevents.AppEventsLogger
import com.livefreebg.android.data.logging.CrashlyticsTree
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class LiveFreeBgApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var crashlyticsTree: CrashlyticsTree

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override fun onCreate() {
        super.onCreate()
        AppEventsLogger.activateApp(this);
        Timber.plant(crashlyticsTree)
    }

    override fun getWorkManagerConfiguration(): Configuration =
        Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()
}
