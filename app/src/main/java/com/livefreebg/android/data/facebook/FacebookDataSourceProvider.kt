package com.livefreebg.android.data.facebook

import com.facebook.AccessToken
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject


class FacebookDataSourceProvider @Inject constructor() {
    suspend fun getUserGroupIds(userId: String): List<String> = suspendCancellableCoroutine { coroutine ->
        GraphRequest(
            AccessToken.getCurrentAccessToken(),
            "/$userId/groups",
            null, HttpMethod.GET,
            {
                coroutine.resumeWith(Result.success(listOf("asdsa", "sadsadsa")))
            }
        ).executeAsync()
    }
}