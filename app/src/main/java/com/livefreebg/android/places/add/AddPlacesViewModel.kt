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

    fun savePlace(description: String) = viewModelScope.launch {
        val currentUiState = uiState.value
        val errorMessage = when {
            description.isEmpty() -> R.string.error_empty_description
            currentUiState.coordinates == null -> R.string.error_no_coordinates
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

    data class UiState(
        val placeSaved: Boolean = false,
        val coordinates: Pair<String, String>? = null,
        val pictures: List<Uri> = emptyList(),
        val errorMessage: String? = null,
    )
}
