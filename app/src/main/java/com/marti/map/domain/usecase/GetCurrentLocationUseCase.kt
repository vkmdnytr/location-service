package com.marti.map.domain.usecase

import com.google.android.gms.maps.model.LatLng
import com.marti.map.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCurrentLocationUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun execute(): Flow<LatLng> {
        return locationRepository.getLocationUpdates()
            .map { location -> 
               location
            }
    }
} 