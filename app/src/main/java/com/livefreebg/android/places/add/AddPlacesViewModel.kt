package com.livefreebg.android.places.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.R
import com.livefreebg.android.common.resources.SimpleStringProvider
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
    private val simpleStringProvider: SimpleStringProvider
) : ViewModel() {
    private val coordinateRegEx =
        Regex("^[-+]?([1-8]?\\d(\\.\\d+)?|90(\\.0+)?),\\s*[-+]?(180(\\.0+)?|((1[0-7]\\d)|([1-9]?\\d))(\\.\\d+)?)\$")

    private val uiStateEmitter = MutableStateFlow(UiState())
    val uiState = uiStateEmitter.asStateFlow()

    fun getCoordinates() = viewModelScope.launch {
        locationProvider.getLastKnownLocation().fold(
            onSuccess = {
                val lat = String.format("%.6f", it.first)
                val lng = String.format("%.6f", it.second)
                uiStateEmitter.emit(UiState(coordinates = lat to lng))
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

    fun savePlace(lat: String, lng: String, description: String) = viewModelScope.launch {
        val currentUiState = uiState.value
        val errorMessage = when {
            description.isEmpty() -> R.string.error_empty_description
            lat.isEmpty()
                    || lng.isEmpty()
                    || !areValidCoordinates(lat, lng)
            -> R.string.error_no_coordinates

            currentUiState.pictures.isEmpty() -> R.string.error_no_pictures
            else -> null
        }?.let { simpleStringProvider.getString(it) }

        if (errorMessage != null) {
            uiStateEmitter.emit(currentUiState.copy(errorMessage = errorMessage))
            return@launch
        }

        val coordinates = uiState.value.coordinates!!
        placesGateway.addPlace(
            Place(
                description = description,
                lat = coordinates.first.toDouble(),
                lng = coordinates.second.toDouble(),
                pictures = uiState.value.pictures.map { it.toString() }
            )
        )
        uiStateEmitter.emit(uiState.value.copy(placeSaved = true))
    }

    private fun areValidCoordinates(
        lat: String,
        lng: String
    ) = coordinateRegEx.matches("$lat,$lng")
    fun saveCoordinates(lat: String, lng: String) {
        uiStateEmitter.value = uiState.value.copy(
            coordinates =  lat to lng
        )
    }

    data class UiState(
        val placeSaved: Boolean = false,
        val coordinates: Pair<String, String>? = null,
        val pictures: List<Uri> = emptyList(),
        val errorMessage: String? = null,
    )
}
