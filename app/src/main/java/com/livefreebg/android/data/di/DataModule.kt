package com.livefreebg.android.data.di

import com.facebook.CallbackManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import com.livefreebg.android.data.firestore.FirestoreUserInfoGateway
import com.livefreebg.android.domain.login.UserInfoGateway
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsUserInfoGateway(userInfoGateway: FirestoreUserInfoGateway): UserInfoGateway

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
    }

}
