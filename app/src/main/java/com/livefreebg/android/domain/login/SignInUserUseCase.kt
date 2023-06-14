package com.livefreebg.android.domain.login

import com.livefreebg.android.data.facebook.FacebookDataSourceProvider
import timber.log.Timber
import javax.inject.Inject

private const val LIVE_FREE_BG_GROUP_ID = "4641938805880941"
class SignInUserUseCase @Inject constructor(
    private val userInfoGateway: UserInfoGateway,
    private val facebookDataSourceProvider: FacebookDataSourceProvider
) {
    suspend fun isUserPartOfLiveFreeGroup(accessToken: String): Boolean {
        val result = userInfoGateway.signInUserWithFacebook(accessToken)
        return result.fold(
            onSuccess = {
                true
            },
            onFailure = {
                Timber.e(it, "Error signing user in!")
                false
            }
        )
    }
}