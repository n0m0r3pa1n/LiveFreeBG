package com.livefreebg.android.data.places

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class FirestorePlace(
    val id: String,
    val pictures: List<String>,
    val lat: Double,
    val lng: Double,
    val description: String
)