package com.marti.map.data.repository

import android.Manifest
import com.google.android.gms.maps.model.LatLng
import com.marti.map.data.datasource.LocationDataSource
import com.marti.map.data.util.PermissionChecker
import com.marti.map.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepositoryImpl @Inject constructor(
    private val locationDataSource: LocationDataSource,
    private val permissionChecker: PermissionChecker
) : LocationRepository {

    override suspend fun getLocationUpdates(): Flow<LatLng> {
        return locationDataSource.getLocationUpdates()
    }

    override suspend fun getCurrentLocation(): Flow<LatLng?> {
        return if (hasLocationPermission()) {
            locationDataSource.getLastLocation()
        } else {
            flow { emit(null) }
        }
    }

    override suspend fun hasLocationPermission(): Boolean {
        return permissionChecker.hasPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    }
}