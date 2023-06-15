package com.livefreebg.android.data.firestore

import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.livefreebg.android.domain.login.UserInfoGateway
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FirestoreUserInfoGateway @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    UserInfoGateway {
    private val userFlow = MutableStateFlow(firebaseAuth.currentUser)
    override fun getCurrentUser(): StateFlow<FirebaseUser?> = userFlow.asStateFlow()
    override suspend fun signInUserWithFacebook(accessToken: String): Result<Boolean> {
        val credential = FacebookAuthProvider.getCredential(accessToken)
        val result = firebaseAuth.signInWithCredential(credential).await()
        return if (result.user != null) {
            userFlow.emit(result.user)
            Result.success(true)
        } else {
            Result.failure(IllegalArgumentException("Authentication failed!"))
        }
    }
}
