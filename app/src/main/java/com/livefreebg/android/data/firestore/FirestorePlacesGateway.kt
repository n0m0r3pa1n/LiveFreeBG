package com.livefreebg.android.data.firestore

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import com.livefreebg.android.data.places.AddPlaceWorker
import com.livefreebg.android.data.workmanager.AppWorkManager
import com.livefreebg.android.data.workmanager.enqueueItem
import com.livefreebg.android.domain.places.Place
import com.livefreebg.android.domain.places.PlacesGateway
import com.squareup.moshi.Moshi
import javax.inject.Inject

class FirestorePlacesGateway @Inject constructor(
    private val workManager: AppWorkManager,
    private val moshi: Moshi,
) : PlacesGateway {
    override fun addPlace(place: Place) {
        workManager.enqueueItem<AddPlaceWorker>(
            tag = AddPlaceWorker.TAG,
            constraints = Constraints.Builder().setRequiredNetworkType(networkType = NetworkType.CONNECTED).build(),
            inputData = Data.Builder()
                .putString(AddPlaceWorker.KEY_PLACE, moshi.adapter(Place::class.java).toJson(place))
                .build(),
        )
    }

    override suspend fun getAllPlaces(): List<Place> {
        return emptyList()
    }
}
