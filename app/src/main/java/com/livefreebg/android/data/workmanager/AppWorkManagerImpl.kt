package com.livefreebg.android.data.workmanager

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import javax.inject.Inject
import javax.inject.Provider

class AppWorkManagerImpl @Inject constructor(
    private val workManager: Provider<WorkManager>,
) : AppWorkManager {

    override fun <T : ListenableWorker> enqueue(
        tag: String,
        requestClass: Class<T>,
        constraints: Constraints,
        inputData: Data,
    ): EnqueueWorkOperation {
        val request = OneTimeWorkRequest.Builder(requestClass)
            .addTag(tag)
            .setConstraints(constraints)
            .setInputData(inputData)
            .build()

        val operation = workManager.get().enqueue(request)
        return EnqueueWorkOperation(workRequestId = request.id, operation = operation)
    }
}
