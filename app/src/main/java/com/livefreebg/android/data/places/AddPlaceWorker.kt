package com.livefreebg.android.data.places

import android.content.Context
import androidx.core.net.toUri
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.firebase.storage.FirebaseStorage
import com.livefreebg.android.domain.places.Place
import com.squareup.moshi.Moshi
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.tasks.await

@HiltWorker
class AddPlaceWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val firebaseStorage: FirebaseStorage,
    private val moshi: Moshi
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        val place = moshi.adapter(Place::class.java).fromJson(inputData.getString(KEY_PLACE)!!)!!
        val file = place.pictures.first().toUri()
        val riversRef = firebaseStorage.reference.child("images/${file.lastPathSegment}")
        return try {
            riversRef.putFile(file).await()
            Result.success()
        } catch (ex: Exception) {
            return Result.retry()
        }
    }

    companion object {
        const val TAG = "AddPlaceWorker"
        const val KEY_PLACE = "PLACE"
    }
}
