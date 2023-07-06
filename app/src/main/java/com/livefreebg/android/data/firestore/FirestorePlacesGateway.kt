package com.livefreebg.android.data.firestore

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import com.google.firebase.firestore.FirebaseFirestore
import com.livefreebg.android.data.places.AddPlaceWorker
import com.livefreebg.android.data.places.FirestorePlace
import com.livefreebg.android.data.workmanager.AppWorkManager
import com.livefreebg.android.data.workmanager.enqueueItem
import com.livefreebg.android.domain.places.Place
import com.livefreebg.android.domain.places.PlacesGateway
import com.squareup.moshi.Moshi
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirestorePlacesGateway @Inject constructor(
    private val workManager: AppWorkManager,
    private val database: FirebaseFirestore,
    private val moshi: Moshi,
) : PlacesGateway {
    override fun addPlace(place: Place) {
        workManager.enqueueItem<AddPlaceWorker>(
            tag = AddPlaceWorker.TAG,
            constraints = Constraints.Builder()
                .setRequiredNetworkType(networkType = NetworkType.CONNECTED).build(),
            inputData = Data.Builder()
                .putString(AddPlaceWorker.KEY_PLACE, moshi.adapter(Place::class.java).toJson(place))
                .build(),
        )
    }

    override suspend fun getAllPlaces(): List<Place> {
        val firestorePlaces = database
            .collection("places")
            .get()
            .await()
            .documents
            .map { it.toObject(FirestorePlace::class.java) }

        return firestorePlaces.mapNotNull { it?.toPlace() }
    }
}
