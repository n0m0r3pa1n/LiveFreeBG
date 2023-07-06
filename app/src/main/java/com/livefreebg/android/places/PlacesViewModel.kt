package com.livefreebg.android.places

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.places.Place
import com.livefreebg.android.domain.places.PlacesGateway
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlacesViewModel @Inject constructor(
    private val placesGateway: PlacesGateway,
) : ViewModel() {
    private val uiStateEmitter = MutableStateFlow(UiState())
    val uiState = uiStateEmitter.asStateFlow()

    init {
        viewModelScope.launch {
            val places = placesGateway.getAllPlaces()
            uiStateEmitter.emit(uiState.value.copy(places = places))
        }
    }

    data class UiState(val places: List<Place> = emptyList())
}