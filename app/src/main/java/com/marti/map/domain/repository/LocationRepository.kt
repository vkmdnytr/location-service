package com.marti.map.domain.repository

import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun getLocationUpdates(): Flow<LatLng>
    suspend fun getCurrentLocation(): Flow<LatLng?>
    suspend fun hasLocationPermission(): Boolean
} 