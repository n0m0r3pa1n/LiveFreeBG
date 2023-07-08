package com.livefreebg.android.places.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

class IntentNavigation @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun navigateToCoordinates(lat: Double, lng: Double) {
        val uri = String.format(Locale.ENGLISH, "geo:%f,%f", lat, lng)
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(uri)).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
        }
        context.startActivity(intent)
    }
}