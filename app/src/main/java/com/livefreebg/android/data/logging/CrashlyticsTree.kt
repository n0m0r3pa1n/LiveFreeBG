package com.livefreebg.android.data.logging

import android.util.Log
import androidx.annotation.VisibleForTesting
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber
import javax.inject.Inject

class CrashlyticsTree @Inject constructor(
    private val crashlytics: FirebaseCrashlytics,
) : Timber.DebugTree() {


    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        super.log(priority, tag, message, throwable)

        when (priority) {
            Log.ERROR -> handleErrors(throwable, priority, message)
            Log.INFO, Log.WARN -> addToExceptionLogs(priority, message, throwable)
        }
    }

    private fun handleErrors(throwable: Throwable?, priority: Int, message: String) {
        if (throwable != null) {
            reportNonFatalException(throwable)
        } else {
            addToExceptionLogs(priority, message)
        }
    }

    private fun reportNonFatalException(throwable: Throwable) {
        crashlytics.recordException(throwable)
    }

    @VisibleForTesting
    internal fun addToExceptionLogs(priority: Int, message: String, throwable: Throwable? = null) =
        crashlytics.log(
            buildString {
                append("${priority.toPriorityTag()} $message")

                if (throwable != null) {
                    append(" ${throwable.stackTraceToString()}")
                }
            },
        )

    private fun Int.toPriorityTag() = when (this) {
        Log.VERBOSE -> "[V]"
        Log.DEBUG -> "[D]"
        Log.INFO -> "[I]"
        Log.WARN -> "[W]"
        Log.ERROR -> "[E]"
        Log.ASSERT -> "[A]"
        else -> ""
    }
}
