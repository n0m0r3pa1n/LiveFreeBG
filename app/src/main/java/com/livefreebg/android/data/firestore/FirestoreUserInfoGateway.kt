package com.livefreebg.android.data.firestore

import android.content.Context
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.livefreebg.android.domain.login.UserInfoGateway
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestoreUserInfoGateway @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    @ApplicationContext private val context: Context
) : UserInfoGateway {
    override fun isUserLoggedIn() = firebaseAuth.currentUser != null
    override suspend fun signInUserWithFacebook(accessToken: String): Result<Boolean> {
        val credential = FacebookAuthProvider.getCredential(accessToken)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return if (result.user != null) {
            Result.success(true)
        } else {
            Result.failure(IllegalArgumentException("Authentication failed!"))
        }
    }
}