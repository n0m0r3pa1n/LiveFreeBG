package com.livefreebg.android.data.facebook

import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.Profile
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject


class FacebookDataSourceProvider @Inject constructor() {
    private val currentUserId: String?
        get() = Profile.getCurrentProfile()?.id

    suspend fun getUserGroupIds(): List<String> = suspendCancellableCoroutine { coroutine ->
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/$currentUserId/groups",
            null, HttpMethod.GET,
            {
                coroutine.resumeWith(Result.success(listOf("asdsa", "sadsadsa")))
            }
        ).executeAsync()
    }
}