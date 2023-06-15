package com.livefreebg.android.domain.login

interface LocationProvider {
    suspend fun getLastKnownLocation(): Result<Pair<Double, Double>>
}
