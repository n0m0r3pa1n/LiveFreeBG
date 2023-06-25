package com.livefreebg.android.common.dispatcher

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

object AndroidDispatcherProvider : DispatcherProvider {

    override fun default(): CoroutineDispatcher {
        return Dispatchers.Default
    }

    override fun io(): CoroutineDispatcher {
        return Dispatchers.IO
    }

    override fun main(): CoroutineDispatcher {
        return Dispatchers.Main
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun singleThreaded(name: String): CoroutineDispatcher {
        return newSingleThreadContext(name)
    }
}
