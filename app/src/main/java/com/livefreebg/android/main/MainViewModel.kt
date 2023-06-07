package com.livefreebg.android.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.login.IsUserAllowedToUseTheAppInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserAllowedToUseTheApp: IsUserAllowedToUseTheAppInteractor
) : ViewModel() {
    val uiState = MutableSharedFlow<UiState>(replay = 1)

    init {
        viewModelScope.launch {
            uiState.emit(UiState(isUserAllowedToUseTheApp()))
        }
    }

    data class UiState(
        val isUserAllowedToUseTheApp: Boolean = false
    )
}