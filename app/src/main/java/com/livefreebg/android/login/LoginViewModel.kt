package com.livefreebg.android.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.livefreebg.android.domain.login.SignInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUserUseCase: SignInUserUseCase,
) : ViewModel(), FacebookCallback<LoginResult> {
    val uiState = MutableStateFlow(UiState())

    fun signInUser(accessToken: String) = viewModelScope.launch {
        val isAllowedToProceed = signInUserUseCase.isUserPartOfLiveFreeGroup(accessToken)
        uiState.emit(uiState.value.copy(isAllowedToProceed = isAllowedToProceed))
    }

    data class UiState(val isAllowedToProceed: Boolean = false)

    override fun onCancel() {

    }

    override fun onError(error: FacebookException) {
    }

    override fun onSuccess(result: LoginResult) {
        signInUser(result.accessToken.token)
    }
}