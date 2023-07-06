package com.livefreebg.android.common.resources

import androidx.annotation.StringRes
import java.util.Locale

interface SimpleStringProvider {
    fun getString(@StringRes resId: Int, locale: Locale? = null): String
    fun getString(@StringRes resId: Int, vararg formatArgs: Any?): String
}
