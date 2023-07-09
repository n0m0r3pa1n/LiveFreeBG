package com.livefreebg.android.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.login.LocationProvider
import com.livefreebg.android.domain.places.Place
import com.livefreebg.android.domain.places.PlacesGateway
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import org.osmdroid.api.IGeoPoint
import org.osmdroid.util.GeoPoint
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placesGateway: PlacesGateway,
    private val locationProvider: LocationProvider,
) : ViewModel() {
    private val uiStateEmitter = MutableStateFlow(UiState())
    val uiState = uiStateEmitter.asStateFlow()

    init {
        viewModelScope.launch {
            placesGateway.getAllPlaces()
                .onEach { uiStateEmitter.emit(uiState.value.copy(places = it)) }
                .launchIn(viewModelScope)
        }
    }

    fun getCoordinates() = viewModelScope.launch {
        locationProvider.getLastKnownLocation().fold(
            onSuccess = {
                uiStateEmitter.emit(
                    uiState.value.copy(
                        center = GeoPoint(it.first, it.second),
                        zoomLevel = 15.00
                    )
                )
            },
            onFailure = {
                Timber.e(it, "Error getting coordinates!")
            }
        )
    }

    fun saveMapState(geoPoint: GeoPoint, zoomLevelDouble: Double) {
        uiStateEmitter.value = uiState.value.copy(
            center = geoPoint,
            zoomLevel = zoomLevelDouble
        )
    }

    data class UiState(
        val center: IGeoPoint? = GeoPoint(
            42.7339,
            25.4858
        ),
        val zoomLevel: Double = 8.3,
        val places: List<Place> = emptyList()
    )
}