package com.livefreebg.android.data.di

import android.content.Context
import com.facebook.CallbackManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.livefreebg.android.data.firestore.FirestorePlacesGateway
import com.livefreebg.android.data.firestore.FirestoreUserInfoGateway
import com.livefreebg.android.data.location.GoogleLocationProvider
import com.livefreebg.android.domain.login.LocationProvider
import com.livefreebg.android.domain.login.UserInfoGateway
import com.livefreebg.android.domain.places.PlacesGateway
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsUserInfoGateway(userInfoGateway: FirestoreUserInfoGateway): UserInfoGateway

    @Binds
    fun bindLocationProvider(googleLocationProvider: GoogleLocationProvider): LocationProvider

    @Binds
    fun bindPlacesGateway(googleLocationProvider: FirestorePlacesGateway): PlacesGateway

    companion object {
        @Provides
        @Singleton
        fun provideFacebookCallbackManager(): CallbackManager = CallbackManager.Factory.create()

        @Provides
        @Singleton
        fun provideFirebaseAuth(): FirebaseAuth = FirebaseAuth.getInstance()

        @Provides
        @Singleton
        fun provideFirebaseStorage(): FirebaseStorage = Firebase.storage


        @Provides
        @Singleton
        fun provideFirebaseCrashlytics(): FirebaseCrashlytics = FirebaseCrashlytics.getInstance()

        @Provides
        fun provideFusedLocationProvider(
            @ApplicationContext context: Context
        ): FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    }

}
