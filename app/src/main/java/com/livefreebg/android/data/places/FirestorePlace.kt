package com.livefreebg.android.data.places

import com.google.firebase.firestore.IgnoreExtraProperties
import com.livefreebg.android.domain.places.Place
import com.squareup.moshi.JsonClass

@IgnoreExtraProperties
@JsonClass(generateAdapter = true)
data class FirestorePlace(
    val id: String? = null,
    val pictures: List<String>? = emptyList(),
    val lat: Double? = null,
    val lng: Double? = null,
    val description: String? = null,
    @field:JvmField
    val isApproved: Boolean? = false
) {
    fun toPlace(): Place? {
        if (id == null || pictures == null || lat == null || lng == null || description == null || isApproved == false) {
            return null
        }

        return Place(
            id = id,
            lat = lat,
            lng = lng,
            description = description,
            pictures = pictures
        )
    }
}