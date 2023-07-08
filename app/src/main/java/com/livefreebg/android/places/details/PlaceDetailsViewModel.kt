package com.livefreebg.android.places.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.places.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlaceDetailsViewModel @Inject constructor() : ViewModel() {

    private val uiStateEmitter = MutableStateFlow(UiState())
    val uiState = uiStateEmitter.asStateFlow()

    fun setPlace(place: Place) {
        viewModelScope.launch {
            uiStateEmitter.emit(UiState(place))
        }
    }

    data class UiState(
        val place: Place? = null
    )
}