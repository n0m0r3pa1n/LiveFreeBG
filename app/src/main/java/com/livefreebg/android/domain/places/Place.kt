package com.livefreebg.android.domain.places

import android.net.Uri

data class Place(
    val lat: Double,
    val lng: Double,
    val description: String,
    val pictures: List<Uri>,
)
