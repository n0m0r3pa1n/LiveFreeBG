package com.livefreebg.android.places.add

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.login.LocationProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class AddPlacesViewModel @Inject constructor(
    private val locationProvider: LocationProvider,
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

    data class UiState(
        val coordinates: Pair<String, String>? = null,
        val pictures: List<Uri> = emptyList()
    )
}
