package com.livefreebg.android.common.resources

import android.content.Context
import android.content.res.Configuration
import androidx.annotation.StringRes
import dagger.Reusable
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.Locale
import javax.inject.Inject

@Reusable
class ResourceSimpleStringProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : SimpleStringProvider {

    override fun getString(resId: Int, locale: Locale?): String = if (locale != null) {
        getLocaleConfigurationContext(resId, locale)
    } else {
        context.getString(resId)
    }

    private fun getLocaleConfigurationContext(
        @StringRes resId: Int,
        requestedLocale: Locale,
    ): String = with(context) {
        Configuration(resources.configuration).run {
            setLocale(requestedLocale)
            createConfigurationContext(this).getString(resId)
        }
    }

    override fun getString(resId: Int, vararg formatArgs: Any?): String = context.getString(resId, *formatArgs)
}
