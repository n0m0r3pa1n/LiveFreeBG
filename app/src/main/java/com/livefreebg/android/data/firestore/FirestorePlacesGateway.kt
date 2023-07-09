package com.livefreebg.android.data.firestore

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.NetworkType
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.livefreebg.android.data.places.AddPlaceWorker
import com.livefreebg.android.data.places.FirestorePlace
import com.livefreebg.android.data.workmanager.AppWorkManager
import com.livefreebg.android.data.workmanager.enqueueItem
import com.livefreebg.android.domain.places.Place
import com.livefreebg.android.domain.places.PlacesGateway
import com.squareup.moshi.Moshi
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.mapNotNull
import timber.log.Timber
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

    override suspend fun getAllPlaces(): Flow<List<Place>> {
        val firestorePlaces = database.collection("places")
        return observeCollection<FirestorePlace>(firestorePlaces).mapNotNull { it.mapNotNull { it.toPlace() } }
    }

    inline fun <reified T> observeCollection(colRef: Query):
            Flow<List<T>> = callbackFlow {

        val subscription = colRef.addSnapshotListener { query, error ->
            if (error != null) {
                trySend(emptyList())
                Timber.e(error, "Error observing collection!")
                return@addSnapshotListener
            }

            val docs = query?.documents?.mapNotNull {
                it.toObject(T::class.java)
            } ?: emptyList()

            trySend(docs)
        }

        awaitClose { subscription.remove() }
    }
}
