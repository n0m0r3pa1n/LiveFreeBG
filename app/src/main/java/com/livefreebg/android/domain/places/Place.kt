package com.livefreebg.android.domain.places

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class Place(
    val lat: Double,
    val lng: Double,
    val description: String,
    val pictures: List<String>,
) : Parcelable
