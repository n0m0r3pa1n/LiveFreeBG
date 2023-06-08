package com.livefreebg.android.domain.login

import com.livefreebg.android.data.facebook.FacebookDataSourceProvider
import javax.inject.Inject

class SignInUserUseCase @Inject constructor(
    private val userInfoGateway: UserInfoGateway,
    private val facebookDataSourceProvider: FacebookDataSourceProvider
) {
    suspend operator fun invoke(accessToken: String) {
        val result = userInfoGateway.signInUserWithFacebook(accessToken)
        result.fold(
            onSuccess = {

            },
            onFailure = {}
        )
    }
}