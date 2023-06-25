package com.livefreebg.android.data.firestore

import com.google.firebase.storage.FirebaseStorage
import com.livefreebg.android.common.dispatcher.DispatcherProvider
import com.livefreebg.android.domain.places.Place
import com.livefreebg.android.domain.places.PlacesGateway
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

class FirestorePlacesGateway @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val firebaseStorage: FirebaseStorage,
) : PlacesGateway {
    override suspend fun addPlace(place: Place) {
        withContext(dispatcherProvider.io()) {
            val file = place.pictures.first()
            val riversRef = firebaseStorage.reference.child("images/${file.lastPathSegment}")
            val uploadTask = riversRef.putFile(file)

            uploadTask.addOnFailureListener {
                Timber.d(it, "### Failure")
            }.addOnSuccessListener { taskSnapshot ->
                Timber.d("### success $taskSnapshot")
            }
        }
    }
}
