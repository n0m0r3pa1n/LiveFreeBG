package com.livefreebg.android.places.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.login.LocationProvider
import com.livefreebg.android.domain.places.Place
import com.livefreebg.android.domain.places.PlacesGateway
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddPlacesViewModel @Inject constructor(
    private val locationProvider: LocationProvider,
    private val placesGateway: PlacesGateway,
) : ViewModel() {
    private val uiStateEmitter = MutableStateFlow(UiState())
    val uiState = uiStateEmitter.asStateFlow()

    fun getCoordinates() = viewModelScope.launch {
        locationProvider.getLastKnownLocation().fold(
            onSuccess = {
                val lat = String.format("%.6f", it.first)
                val lng = String.format("%.6f", it.second)
                uiStateEmitter.emit(UiState(lat to lng))
            },
            onFailure = {
                Timber.e(it, "Error getting coordinates!")
            }
        )
    }

    fun setPictures(uris: List<@JvmSuppressWildcards Uri>) = viewModelScope.launch {
        uiStateEmitter.emit(
            uiState.value.copy(pictures = uris)
        )
    }

    fun savePlace(description: String) = viewModelScope.launch {
        val coordinates = uiState.value.coordinates!!
        placesGateway.addPlace(
            Place(
                description = description,
                lat = coordinates.first.toDouble(),
                lng = coordinates.second.toDouble(),
                pictures = uiState.value.pictures
            )
        )
    }

    data class UiState(
        val coordinates: Pair<String, String>? = null,
        val pictures: List<Uri> = emptyList(),
    )
}
