package com.livefreebg.android.domain.login

interface UserInfoGateway {
    fun isUserLoggedIn(): Boolean
    suspend fun signInUserWithFacebook(accessToken: String): Result<Boolean>
}