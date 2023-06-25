package com.livefreebg.android.domain.places

interface PlacesGateway {
    fun addPlace(place: Place)

    suspend fun getAllPlaces(): List<Place>
}
