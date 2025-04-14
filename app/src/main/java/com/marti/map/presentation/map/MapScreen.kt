package com.marti.map.presentation.map

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.model.CameraPosition
import com.google.maps.android.compose.*
import com.marti.map.R
import com.marti.map.presentation.common.BottomSpace
import kotlinx.coroutines.launch


@Composable
fun MapScreen(
		viewModel: MapViewModel = hiltViewModel()
) {
		val context = LocalContext.current
		val uiState by viewModel.uiState.collectAsState()
		val cameraPositionState = rememberCameraPositionState()

		// Handle lifecycle events
		MapLifecycleHandler(viewModel)

		Box(modifier = Modifier.fillMaxSize()) {
				when (uiState.pageStatus) {
						PageStatus.ShowUI -> {
								MapContent(
										uiState = uiState,
										context = context,
										viewModel = viewModel,
										cameraPositionState = cameraPositionState,
								)
						}

						PageStatus.Loading -> LoadingIndicator()
				}

				when (uiState.dialogStatus) {
						DialogStatus.ERROR_DIALOG -> ErrorDialog(
								error = uiState.error,
								onDismiss = { viewModel.setDialog(DialogStatus.DEFAULT) },
						)

						DialogStatus.SHOW_APP_SETTING_DIALOG -> SettingsDialog(
								onDismiss = { viewModel.setDialog(DialogStatus.DEFAULT) },
								context = context
						)

						DialogStatus.DEFAULT -> { /* No dialog */
						}
				}
		}
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
private fun MapLifecycleHandler(viewModel: MapViewModel) {
		val lifecycleOwner = LocalLifecycleOwner.current
		val permissionState = rememberPermissionState(
				Manifest.permission.ACCESS_FINE_LOCATION
		) { isGranted ->
				viewModel.updateHasPermission(isGranted)
				viewModel.updateLocationPermission(isGranted)
		}
		DisposableEffect(permissionState) {
				if (!viewModel.uiState.value.hasLocationPermission && !permissionState.status.isGranted) {
						if (permissionState.status.shouldShowRationale) {
								viewModel.setDialog(DialogStatus.SHOW_APP_SETTING_DIALOG)
						} else {
								permissionState.launchPermissionRequest()
						}
				}
				onDispose { }
		}

		DisposableEffect(lifecycleOwner) {
				val observer = LifecycleEventObserver { _, event ->
						when (event) {
								Lifecycle.Event.ON_RESUME -> {
										viewModel.onResume()
								}

								else -> { /* Handle other events if needed */
								}
						}
				}

				lifecycleOwner.lifecycle.addObserver(observer)
				onDispose {
						lifecycleOwner.lifecycle.removeObserver(observer)
				}
		}
}

@Composable
private fun MapContent(
		uiState: MapUiState,
		modifier: Modifier = Modifier,
		context: Context,
		viewModel: MapViewModel,
		cameraPositionState: CameraPositionState
) {
		try {
				MapsInitializer.initialize(context)
		} catch (e: Exception) {
				viewModel.setError(e)
		}
		LaunchedEffect(uiState.currentLocation) {
				try {
						cameraPositionState.animate(
								CameraUpdateFactory.newCameraPosition(
										CameraPosition.Builder()
												.target(uiState.currentLocation)
												.zoom(uiState.zoom)
												.build()
								)
						)
				} catch (e: Exception) {
						viewModel.setError(e)
				}
		}

		Box {
				GoogleMap(
						modifier = modifier.fillMaxSize(),
						cameraPositionState = cameraPositionState,
						properties = MapProperties(
								isMyLocationEnabled = uiState.hasLocationPermission
						),
						uiSettings = MapUiSettings(
								myLocationButtonEnabled = true,
								zoomControlsEnabled = true
						)
				) {
						Polyline(
								points = uiState.markerList.map { it },
								color = MaterialTheme.colorScheme.primary,
								width = 10f
						)
						uiState.markerList.forEach { location ->
								Marker(
										state = MarkerState(position = location),
										title = stringResource(R.string.location),
										snippet = "$location"
								)
						}
				}
				Card(
						modifier = Modifier
								.fillMaxWidth()
								.align(Alignment.BottomEnd)
								.padding(horizontal = 0.dp),
						colors = CardDefaults.cardColors(
								containerColor = MaterialTheme.colorScheme.surfaceVariant
						)
				) {
						Column(
								modifier = Modifier
										.fillMaxWidth()
										.padding(16.dp),
								horizontalAlignment = Alignment.CenterHorizontally
						) {

								Button(
										modifier = Modifier
												.fillMaxWidth()
												.padding(horizontal = 16.dp),
										onClick = { viewModel.clearRoutes() }
								) {
										Text(stringResource(R.string.delete_route))
								}
								Spacer(Modifier.padding(top = 10.dp))
								Button(
										modifier = Modifier
												.fillMaxWidth()
												.padding(horizontal = 16.dp),
										onClick = { viewModel.startTracking() }
								) {
										Text(stringResource(R.string.enable_location_tracking))
								}
								Spacer(Modifier.padding(top = 10.dp))
								Button(
										modifier = Modifier
												.fillMaxWidth()
												.padding(horizontal = 16.dp),
										onClick = { viewModel.stopTracking() }
								) {
										Text(stringResource(R.string.disable_location_tracking))
								}
								BottomSpace()
						}
				}
		}

}

@Composable
private fun LoadingIndicator() {
		Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center
		) {
				CircularProgressIndicator()
		}
}


@Composable
private fun ErrorDialog(
		error: Error?,
		onDismiss: () -> Unit
) {
		AlertDialog(
				onDismissRequest = onDismiss,
				title = {
						Text(
								text = stringResource(R.string.error),
								style = MaterialTheme.typography.titleLarge
						)
				},
				text = {
						Text(
								text = error?.message ?: stringResource(R.string.not_know_error),
								style = MaterialTheme.typography.bodyMedium
						)
				},
				confirmButton = {
						TextButton(onClick = onDismiss) {
								Text(stringResource(R.string.ok))
						}
				}
		)
}

@Composable
private fun SettingsDialog(
		onDismiss: () -> Unit,
		context: Context
) {
		val scope = rememberCoroutineScope()

		AlertDialog(
				onDismissRequest = onDismiss,
				title = {
						Text(
								text = stringResource(R.string.dialog_title_need_location_permission),
								style = MaterialTheme.typography.titleLarge
						)
				},
				text = {
						Text(
								text = stringResource(R.string.dialog_message_need_location_permission),
								style = MaterialTheme.typography.bodyMedium
						)
				},
				confirmButton = {
						TextButton(
								onClick = {
										scope.launch {
												try {
														context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
																flags = Intent.FLAG_ACTIVITY_NEW_TASK
														})
												} catch (e: Exception) {
														try {
																context.startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
																		data = Uri.fromParts("package", context.packageName, null)
																		flags = Intent.FLAG_ACTIVITY_NEW_TASK
																})
														} catch (e: Exception) {
																context.startActivity(Intent(Settings.ACTION_SETTINGS).apply {
																		flags = Intent.FLAG_ACTIVITY_NEW_TASK
																})
														}
														onDismiss()
												}
										}

								}
						) {
								Text(stringResource(R.string.give_permission))
						}
				}
		)
}

