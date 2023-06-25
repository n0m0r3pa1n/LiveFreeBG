package com.livefreebg.android.common.extensions

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

fun <T> AppCompatActivity.observeViewState(flow: Flow<T>, block: (T) -> Unit) {
    lifecycleScope.launch {
        repeatOnLifecycle(STARTED) {
            flow.collect {
                block(it)
            }
        }
    }
}

fun <T> Fragment.observeViewState(flow: Flow<T>, block: (T) -> Unit) {
    viewLifecycleOwner.lifecycleScope.launch {
        viewLifecycleOwner.repeatOnLifecycle(STARTED) {
            flow.collect {
                block(it)
            }
        }
    }
}
