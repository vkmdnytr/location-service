package com.marti.map.di


import android.content.Context
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.marti.map.data.datasource.LocationDataSource
import com.marti.map.data.repository.LocationRepositoryImpl
import com.marti.map.data.util.PermissionChecker
import com.marti.map.domain.repository.LocationRepository
import com.marti.map.domain.usecase.ChangeMarkerForHelper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object LocationModule {
    
    @Provides
    @Singleton
    fun provideLocationDataSource(
        @ApplicationContext context: Context
    ): LocationDataSource {
        return LocationDataSource(context)
    }

    @Provides
    @Singleton
    fun providePermissionChecker(
        @ApplicationContext context: Context
    ): PermissionChecker {
        return PermissionChecker(context)
    }

    @Provides
    @Singleton
    fun provideLocationRepository(
        locationDataSource: LocationDataSource,
        permissionChecker: PermissionChecker
    ): LocationRepository {
        return LocationRepositoryImpl(locationDataSource, permissionChecker)
    }
    @Provides
    @Singleton
    fun provideFusedLocationProviderClient(
        @ApplicationContext context: Context
    ): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(context)
    }

}