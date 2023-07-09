package com.livefreebg.android.domain.places

import kotlinx.coroutines.flow.Flow

interface PlacesGateway {
    fun addPlace(place: Place)

    suspend fun getAllPlaces(): Flow<List<Place>>
}
