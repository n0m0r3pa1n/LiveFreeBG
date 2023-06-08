package com.livefreebg.android.domain.login

interface UserInfoGateway {
    suspend fun signInUserWithFacebook(accessToken: String): Result<Boolean>
}