package com.livefreebg.android.domain.login

import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface UserInfoGateway {
    fun getCurrentUser(): Flow<FirebaseUser?>
    suspend fun signInUserWithFacebook(accessToken: String): Result<Boolean>
}
