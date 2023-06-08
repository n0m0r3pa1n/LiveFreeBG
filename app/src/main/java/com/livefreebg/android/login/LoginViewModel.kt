package com.livefreebg.android.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.livefreebg.android.domain.login.SignInUserUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val signInUserUseCase: SignInUserUseCase
) : ViewModel() {
    fun signInUser(accessToken: String) = viewModelScope.launch {
        signInUserUseCase(accessToken)
    }
}