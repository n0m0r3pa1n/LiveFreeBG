package com.livefreebg.android.data.di

import com.livefreebg.android.domain.places.Place
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface MoshiModule {
    companion object {
        @Provides
        @Singleton
        fun provideMoshi() = Moshi.Builder().build()

        @Provides
        fun providePlaceAdapter(moshi: Moshi): JsonAdapter<Place> = moshi.adapter(Place::class.java)
    }
}
