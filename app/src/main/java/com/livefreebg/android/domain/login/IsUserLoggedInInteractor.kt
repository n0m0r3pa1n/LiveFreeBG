package com.livefreebg.android.domain.login

import javax.inject.Inject

class IsUserLoggedInInteractor @Inject constructor(private val userInfoGateway: UserInfoGateway) {
    operator fun invoke(): Boolean = userInfoGateway.isUserLoggedIn()
}