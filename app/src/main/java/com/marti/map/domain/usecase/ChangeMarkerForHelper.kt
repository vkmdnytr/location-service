package com.marti.map.domain.usecase


import android.util.Log
import com.google.android.gms.maps.model.LatLng
import com.marti.map.presentation.map.TrackingData
import com.marti.map.utils.LocationHelper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChangeMarkerForHelper @Inject constructor() {

		private val _changeMarkerUI = MutableStateFlow(
				TrackingData(
						currentLocation = null,
						routePoints = arrayListOf(),
						changedMarker = false
				)
		)
		val changeMarkerUI: StateFlow<TrackingData> = _changeMarkerUI.asStateFlow()


		fun setCurrentLocation(latLng: LatLng) {
				val newList =
						if (LocationHelper.isWithin100Meters(latLng, changeMarkerUI.value.currentLocation)) {
								val list = changeMarkerUI.value.routePoints.apply { add(latLng) }
								arrayListOf<LatLng>().apply { addAll(list) }
						} else {
								changeMarkerUI.value.routePoints
						}

				_changeMarkerUI.value = changeMarkerUI.value.copy(
						changedMarker = LocationHelper.isWithin100Meters(
								latLng,
								changeMarkerUI.value.currentLocation
						),
						routePoints = newList,
						currentLocation = latLng,
				)
				Log.d("TAG -> setCurrentLocation", "$newList")
		}

		fun clearRoutesMarker() {
				val newList = changeMarkerUI.value.currentLocation?.let {
						arrayListOf<LatLng>().apply {
								add(it)
						}
				} ?: arrayListOf()
				_changeMarkerUI.value = changeMarkerUI.value.copy(
						routePoints = newList,
						changedMarker = false
				)
		}


} 