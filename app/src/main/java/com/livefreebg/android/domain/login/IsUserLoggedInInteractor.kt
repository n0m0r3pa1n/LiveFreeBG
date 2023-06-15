package com.livefreebg.android.domain.login

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class IsUserLoggedInInteractor @Inject constructor(private val userInfoGateway: UserInfoGateway) {
    operator fun invoke(): Flow<Boolean> = userInfoGateway.getCurrentUser().map { it != null }
}
