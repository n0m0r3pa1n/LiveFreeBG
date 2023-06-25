package com.livefreebg.android.data.places

import android.content.Context
import android.net.Uri
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.storage.FirebaseStorage
import com.livefreebg.android.domain.places.Place
import com.squareup.moshi.JsonAdapter
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import javax.inject.Provider

@HiltWorker
class AddPlaceWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val firebaseStorage: FirebaseStorage,
    private val placeAdapter: Provider<JsonAdapter<Place>>,
) : CoroutineWorker(appContext, workerParams) {

    private val uploadedPictures = mutableListOf<String>()
    override suspend fun doWork(): Result {
        val place = placeAdapter.get().fromJson(inputData.getString(KEY_PLACE)!!)!!
        val picturesToUpload = place.pictures - uploadedPictures
        picturesToUpload.forEach {
            try {
                uploadFile(it.toUri())
                uploadedPictures.add(it)
            } catch (ex: Exception) {
                Timber.e(ex, "Error uploading picture")
                return Result.retry()
            }
        }

        return Result.success()
    }

    private suspend fun uploadFile(uri: Uri) {
        firebaseStorage.reference
            .child("images/${uri.lastPathSegment}")
            .putFile(uri)
            .await()
    }

    companion object {
        const val TAG = "AddPlaceWorker"
        const val KEY_PLACE = "PLACE"
    }
}
