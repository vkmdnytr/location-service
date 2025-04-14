package com.marti.map.data.datasource

import android.content.Context
import android.os.Looper
import com.google.android.gms.location.*
import com.google.android.gms.maps.model.LatLng
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject

class LocationDataSource @Inject constructor(
    @ApplicationContext private val context: Context
) {
    private val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    private val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
        .setWaitForAccurateLocation(false)
        .setMinUpdateIntervalMillis(500L)
        .setMaxUpdateDelayMillis(1000L)
        .build()

    suspend fun getLocationUpdates(): Flow<LatLng> = callbackFlow {
        val locationCallback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { location ->
                    trySend(LatLng(location.latitude, location.longitude))
                }
            }
        }

        try {
            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            ).addOnFailureListener { e ->
                close(e)
            }
        } catch (e: SecurityException) {
            close(e)
        }

        awaitClose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }

    suspend fun getLastLocation(): Flow<LatLng?> = callbackFlow {
        try {
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location ->
                    if (location != null) {
                        trySend(LatLng(location.latitude, location.longitude))
                    } else {
                        trySend(null)
                    }
                }
                .addOnFailureListener {
                    trySend(null)
                }
        } catch (e: SecurityException) {
            trySend(null)
        }

        awaitClose()
    }
} 