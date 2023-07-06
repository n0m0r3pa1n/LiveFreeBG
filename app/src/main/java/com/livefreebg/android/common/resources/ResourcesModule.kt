package com.livefreebg.android.common.resources

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ResourcesModule {

    @Provides
    @Singleton
    fun bindSimpleStringProvider(impl: ResourceSimpleStringProvider): SimpleStringProvider = impl
}
