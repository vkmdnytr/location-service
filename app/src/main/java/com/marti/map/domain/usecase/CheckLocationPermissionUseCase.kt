package com.marti.map.domain.usecase

import com.marti.map.domain.repository.LocationRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CheckLocationPermissionUseCase @Inject constructor(
    private val locationRepository: LocationRepository
) {
    suspend fun execute(): Flow<Boolean> {
        return flow {
            emit(locationRepository.hasLocationPermission())
        }
    }
} 