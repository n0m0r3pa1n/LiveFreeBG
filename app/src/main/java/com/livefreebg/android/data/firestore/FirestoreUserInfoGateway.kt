package com.livefreebg.android.data.firestore

import com.google.firebase.auth.FirebaseAuth
import com.livefreebg.android.domain.login.UserInfoGateway
import javax.inject.Inject

class FirestoreUserInfoGateway @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : UserInfoGateway {
    fun isUserLoggedIn() = firebaseAuth.currentUser != null


}