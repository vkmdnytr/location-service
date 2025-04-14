package com.marti.map.service

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.marti.map.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class LocationServiceManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun startTracking() {
        Toast.makeText(context,
            context.getString(R.string.start_location_service), Toast.LENGTH_LONG).show()
        ContextCompat.startForegroundService(context, Intent(context, LocationTrackingService::class.java))
    }

    fun stopTracking() {
        Toast.makeText(context, context.getString(R.string.stop_location_service), Toast.LENGTH_LONG).show()
        context.stopService(Intent(context, LocationTrackingService::class.java))
    }
} 