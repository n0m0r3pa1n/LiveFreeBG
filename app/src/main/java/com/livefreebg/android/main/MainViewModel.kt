package com.livefreebg.android.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.R
import com.livefreebg.android.domain.login.IsUserAllowedToUseTheAppInteractor
import com.livefreebg.android.domain.login.IsUserLoggedInInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val isUserAllowedToUseTheApp: IsUserAllowedToUseTheAppInteractor,
    private val isUserLoggedInInteractor: IsUserLoggedInInteractor,
) : ViewModel() {
    val uiState = MutableStateFlow(UiState())

    init {
        viewModelScope.launch {
            isUserLoggedInInteractor()
                .onEach {
                    val startDestinationId = if (it) {
                        R.id.PlacesFragment
                    } else {
                        R.id.LoginFragment
                    }
                    uiState.emit(UiState(startDestinationId))
                }
                .launchIn(viewModelScope)
        }
    }

    data class UiState(val startDestinationId: Int? = null)
}
