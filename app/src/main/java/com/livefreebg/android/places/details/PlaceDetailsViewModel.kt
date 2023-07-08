package com.livefreebg.android.places.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.places.Place
import com.livefreebg.android.places.utils.IntentNavigation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class PlaceDetailsViewModel @Inject constructor(
    private val intentNavigation: IntentNavigation,
) : ViewModel() {

    private val uiStateEmitter = MutableStateFlow(UiState())
    val uiState = uiStateEmitter.asStateFlow()

    private val place: Place
        get() = uiState.value.place!!

    fun setPlace(place: Place) {
        viewModelScope.launch {
            uiStateEmitter.emit(UiState(place))
        }
    }

    fun navigate() {
        intentNavigation.navigateToCoordinates(place.lat, place.lng)
    }

    data class UiState(
        val place: Place? = null,
    )
}