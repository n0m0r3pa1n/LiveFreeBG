package com.livefreebg.android.domain.login

import javax.inject.Inject

class IsUserAllowedToUseTheAppInteractor @Inject constructor() {
    suspend operator fun invoke() = true
}