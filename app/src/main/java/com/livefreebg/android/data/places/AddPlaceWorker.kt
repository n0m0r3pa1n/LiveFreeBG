package com.livefreebg.android.data.places

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.livefreebg.android.domain.places.Place
import com.squareup.moshi.JsonAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.util.UUID
import javax.inject.Provider

@HiltWorker
class AddPlaceWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val firebaseFirestore: FirebaseFirestore,
    private val firebaseStorage: FirebaseStorage,
    private val placeAdapter: Provider<JsonAdapter<Place>>,
) : CoroutineWorker(appContext, workerParams) {

    private val uploadedPictures = mutableListOf<UploadedPicture>()
    private val database = firebaseFirestore

    override suspend fun doWork(): Result {
        val place: Place = placeAdapter.get().fromJson(inputData.getString(KEY_PLACE)!!)!!
        val picturesToUpload = place.pictures - uploadedPictures.map { it.path }.toSet()
        picturesToUpload.forEach {
            try {
                val uri = it.toUri()
                val fileName = uri.toFileName()
                uploadFile(uri, fileName)
                uploadedPictures.add(UploadedPicture(it, fileName))
            } catch (ex: Exception) {
                Timber.e(ex, "Error uploading picture")
                return Result.retry()
            }
        }

        try {
            savePlace(place)
        } catch (ex: Exception) {
            Timber.e(ex, "Error saving place!")
            return Result.retry()
        }

        return Result.success()
    }

    private suspend fun savePlace(place: Place) {
        val firestorePlace = place.toFirestorePlace(uploadedPictures.map { it.fileName })
        database
            .collection("places")
            .document(firestorePlace.id)
            .set(firestorePlace)
            .await()
    }

    private suspend fun uploadFile(uri: Uri, fileName: String) {
        firebaseStorage.reference
            .child("images/${fileName}")
            .putFile(uri)
            .await()
    }

    private fun Uri.toFileName() = UUID.randomUUID().toString() + lastPathSegment

    private fun Place.toFirestorePlace(files: List<String>) = FirestorePlace(
        id = UUID.randomUUID().toString(),
        pictures = files,
        lat = lat,
        lng = lng,
        description = description
    )
    data class UploadedPicture(val path: String, val fileName: String)

    companion object {
        const val TAG = "AddPlaceWorker"
        const val KEY_PLACE = "PLACE"
    }
}
