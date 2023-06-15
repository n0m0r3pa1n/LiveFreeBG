package com.livefreebg.android.data.location

import com.google.android.gms.location.FusedLocationProviderClient
import com.livefreebg.android.domain.login.LocationProvider
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject

class GoogleLocationProvider @Inject constructor(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
) : LocationProvider {

    @Suppress("MissingPermission")
    override suspend fun getLastKnownLocation(): Result<Pair<Double, Double>> = suspendCancellableCoroutine { continuation ->
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                continuation.resumeWith(
                    Result.success(
                        Result.success(it.latitude to it.longitude)
                    )
                )
            }
            .addOnFailureListener {
                continuation.resumeWith(Result.failure(it))
            }
    }
}
