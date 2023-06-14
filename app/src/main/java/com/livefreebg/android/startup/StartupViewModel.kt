package com.livefreebg.android.startup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.login.IsUserLoggedInInteractor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val isUserLoggedInInteractor: IsUserLoggedInInteractor
) : ViewModel() {
    val navigateToLogin = MutableSharedFlow<Unit>(replay = 1)
    val navigateToPlaces = MutableSharedFlow<Unit>(replay = 1)

    init {
        viewModelScope.launch {
            if (isUserLoggedInInteractor()) {
                navigateToPlaces.emit(Unit)
            } else {
                navigateToLogin.emit(Unit)
            }
        }
    }
}