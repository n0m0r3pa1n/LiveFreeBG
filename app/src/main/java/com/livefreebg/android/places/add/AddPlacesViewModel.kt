package com.livefreebg.android.places.add

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
                uiStateEmitter.emit(UiState(it))
            },
            onFailure = {
                Timber.e(it, "Error getting coordinates!")
            }
        )
    }

    data class UiState(val coordinates: Pair<Double, Double>? = null)
}
