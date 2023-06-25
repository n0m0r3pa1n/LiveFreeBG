package com.livefreebg.android.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher

interface DispatcherProvider {

    fun default(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun main(): CoroutineDispatcher

    fun singleThreaded(name: String): CoroutineDispatcher
}
