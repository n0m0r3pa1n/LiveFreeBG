package com.livefreebg.android.data.workmanager

import androidx.work.Constraints
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Operation
import java.util.UUID

interface AppWorkManager {
    fun <T : ListenableWorker> enqueue(
        tag: String,
        requestClass: Class<T>,
        constraints: Constraints = Constraints(),
        inputData: Data,
    ): EnqueueWorkOperation
}

inline fun <reified W : ListenableWorker> AppWorkManager.enqueueItem(
    tag: String,
    constraints: Constraints = Constraints(),
    inputData: Data,
) = enqueue(tag, W::class.java, constraints, inputData)

data class EnqueueWorkOperation(
    val workRequestId: UUID,
    val operation: Operation,
)
