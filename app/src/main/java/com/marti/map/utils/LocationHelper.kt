package com.marti.map.utils

import com.google.android.gms.maps.model.LatLng
import kotlin.math.*

object LocationHelper {

		private const val EARTH_RADIUS = 6371000.0 // 6,371 kilometers
		private const val METER_100 = 100 // 6,371 kilometers

		private fun calculateDistanceInMeters(start: LatLng, end: LatLng): Double {

				val dLat = Math.toRadians(end.latitude - start.latitude)
				val dLon = Math.toRadians(end.longitude - start.longitude)

				val lat1 = Math.toRadians(start.latitude)
				val lat2 = Math.toRadians(end.latitude)

				val a = sin(dLat / 2).pow(2.0) + cos(lat1) * cos(lat2) * sin(dLon / 2).pow(2.0)
				val c = 2 * atan2(sqrt(a), sqrt(1 - a))

				return EARTH_RADIUS * c
		}

		fun isWithin100Meters(start: LatLng?, end: LatLng?): Boolean = if (start == null) {
				false
		} else if (end == null) {
				true
		} else {
				val distance = calculateDistanceInMeters(start, end)
				METER_100
				distance > 1
		}

}