package com.marti.map.presentation.map

import com.google.android.gms.maps.model.LatLng

data class MapUiState(
		val zoom: Float,
		val currentLocation: LatLng,
		val markerList:List<LatLng> = emptyList(),
		val pageStatus: PageStatus,
		val dialogStatus: DialogStatus,
		val hasLocationPermission: Boolean,
		val error: Error? = null
)

enum class PageStatus {
		ShowUI,
		Loading
}

enum class DialogStatus {
		DEFAULT,
		ERROR_DIALOG,
		SHOW_APP_SETTING_DIALOG
}

data class TrackingData(
		val routePoints:ArrayList<LatLng> = arrayListOf(),
		val currentLocation: LatLng?,
		val changedMarker: Boolean
)

data class Error(val message: String, val errorCode: Int)