package com.livefreebg.android.domain.places

interface PlacesGateway {
    suspend fun addPlace(place: Place)
}
