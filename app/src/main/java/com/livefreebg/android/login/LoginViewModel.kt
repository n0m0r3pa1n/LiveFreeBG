package com.livefreebg.android.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.login.IsUserLoggedInInteractor
import com.livefreebg.android.domain.login.SignInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUserUseCase: SignInUserUseCase,
    isUserLoggedIn: IsUserLoggedInInteractor,
) : ViewModel() {
    val uiState = MutableStateFlow(UiState(isLoggedIn = isUserLoggedIn()))

    fun signInUser(accessToken: String) = viewModelScope.launch {
        val isAllowedToProceed = signInUserUseCase.isUserPartOfLiveFreeGroup(accessToken)
        uiState.emit(uiState.value.copy(isAllowedToProceed = isAllowedToProceed))
    }

    data class UiState(val isLoggedIn: Boolean, val isAllowedToProceed: Boolean? = null) {

    }
}