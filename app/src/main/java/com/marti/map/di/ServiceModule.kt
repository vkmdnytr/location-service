package com.marti.map.di

import android.content.Context
import com.marti.map.service.LocationServiceManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ServiceModule {

    @Provides
    @Singleton
    fun provideLocationServiceManager(
        @ApplicationContext context: Context
    ): LocationServiceManager {
        return LocationServiceManager(context)
    }
} 