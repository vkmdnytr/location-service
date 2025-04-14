package com.marti.map.presentation.map

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.maps.model.LatLng
import com.marti.map.domain.usecase.ChangeMarkerForHelper
import com.marti.map.domain.usecase.CheckLocationPermissionUseCase
import com.marti.map.domain.usecase.GetCurrentLocationUseCase
import com.marti.map.service.LocationServiceManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MapViewModel @Inject constructor(
		private val changeMarkerForHelper: ChangeMarkerForHelper,
		private val locationServiceManager: LocationServiceManager,
		private val getCurrentLocationUseCase: GetCurrentLocationUseCase,
		private val checkLocationPermissionUseCase: CheckLocationPermissionUseCase,
) : ViewModel() {

		companion object {
				private val DEFAULT_LOCATION = LatLng(41.0082, 28.9784) // Istanbul
				private const val DEFAULT_ZOOM = 100f
		}

		private val _uiState = MutableStateFlow(
				MapUiState(
						currentLocation = DEFAULT_LOCATION,
						zoom = DEFAULT_ZOOM,
						pageStatus = PageStatus.ShowUI,

						hasLocationPermission = false,
						dialogStatus = DialogStatus.DEFAULT
				)
		)
		val uiState: StateFlow<MapUiState> = _uiState.asStateFlow()

		init {
				checkPermission()
				controlChangeMarker()
		}

		private fun controlChangeMarker() {
				viewModelScope.launch {
						changeMarkerForHelper.changeMarkerUI.collectLatest {
								if(it.routePoints != uiState.value.markerList ){
										_uiState.value = _uiState.value.copy(
												error = null,
												currentLocation = it.currentLocation?: DEFAULT_LOCATION ,
												dialogStatus = DialogStatus.DEFAULT,
												pageStatus = PageStatus.ShowUI,
												markerList = it.routePoints
										)
								}
								Log.d("TAG -> routePoints ",  it.routePoints.toString())
						}
				}
		}

		private fun startLocationUpdates() {
				viewModelScope.launch {
						setLoading()
						getCurrentLocationUseCase
								.execute()
								.catch {
										setError(it)
								}
								.collectLatest { result ->
										_uiState.value = _uiState.value.copy(
												error = null,
												currentLocation = result,
												dialogStatus = DialogStatus.DEFAULT,
												pageStatus = PageStatus.ShowUI
										)
								}
				}
		}

		private fun setLoading() {
				_uiState.value = _uiState.value.copy(pageStatus = PageStatus.Loading)
		}

		private fun setError(it: Throwable) {
				_uiState.value = _uiState.value.copy(
						error = Error(
								errorCode = 404,
								message = it.message ?: it.localizedMessage ?: "Not Found"
						),
						pageStatus = PageStatus.ShowUI,
						dialogStatus = DialogStatus.ERROR_DIALOG
				)
		}


	fun updateLocationPermission(hasLocationPermission: Boolean) {
			viewModelScope.launch {
					if (hasLocationPermission) {
							startLocationUpdates()
					} else {
							_uiState.value = _uiState.value.copy(
									dialogStatus = DialogStatus.SHOW_APP_SETTING_DIALOG,
									pageStatus = PageStatus.ShowUI
							)
							locationServiceManager.stopTracking()
					}
			}
		}

		fun onResume() {
				viewModelScope.launch {
						checkLocationPermissionUseCase
								.execute()
								.onStart { }
								.collectLatest {
										if (uiState.value.hasLocationPermission != it) {
												checkPermission()
												updateLocationPermission(uiState.value.hasLocationPermission)
										}
								}
				}
		}

		private fun checkPermission() {
				viewModelScope.launch {
						setLoading()
						checkLocationPermissionUseCase
								.execute()
								.catch { setError(it) }
								.collectLatest {
										updateHasPermission(it)
										updateLocationPermission(hasLocationPermission = it)
								}
				}
		}

		fun updateHasPermission(hasLocationPermission: Boolean) {
				_uiState.value = _uiState.value.copy(
						hasLocationPermission = hasLocationPermission
				)
		}

		fun setError(error: Exception) {
				_uiState.value = _uiState.value.copy(
						error = Error(
								errorCode = error.hashCode(),
								message = error.message ?: error.localizedMessage ?: "Not Found"
						)
				)
		}

		fun setDialog(dialogStatus: DialogStatus) {
				_uiState.value = _uiState.value.copy(
						dialogStatus = dialogStatus
				)
		}

		fun clearRoutes() {
				changeMarkerForHelper.clearRoutesMarker()
		}

		fun stopTracking() {
				viewModelScope.launch {
						locationServiceManager.stopTracking()
				}
		}

		fun startTracking() {
				viewModelScope.launch {
						locationServiceManager.startTracking()
				}
		}

}
