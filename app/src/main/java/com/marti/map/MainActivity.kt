package com.marti.map

import android.Manifest
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.marti.map.navigation.Screen
import com.marti.map.presentation.home.HomeScreen
import com.marti.map.presentation.map.MapScreen
import com.marti.map.presentation.setting.SettingsScreen
import com.marti.map.presentation.splash.SplashScreen
import com.marti.map.presentation.theme.MartiMapTheme
import com.marti.map.presentation.components.PermissionExplanationDialog
import com.marti.map.presentation.components.PermissionDeniedDialog
import com.marti.map.domain.model.NotificationPermissionState
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

	private val viewModel: MainViewModel by viewModels()

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	private val permissionLauncher = registerForActivityResult(
		ActivityResultContracts.RequestPermission()
	) { isGranted ->
			viewModel.onPermissionResult(
			isGranted = isGranted,
			shouldShowRationale = shouldShowRequestPermissionRationale(
				Manifest.permission.POST_NOTIFICATIONS
			)
		)
	}

	@RequiresApi(Build.VERSION_CODES.TIRAMISU)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			val navController = rememberNavController()

			MartiMapTheme(darkTheme = viewModel.isDarkMode) {
				val permissionState by viewModel.permissionState.collectAsState()
				
				Surface(
					modifier = Modifier.fillMaxSize(),
					color = MaterialTheme.colorScheme.background
				) {
					NavHost(
						navController = navController,
						startDestination = Screen.Splash.route
					) {
						composable(Screen.Splash.route) {
							SplashScreen(
								onSplashComplete = {
									navController.navigate(Screen.Home.route) {
										popUpTo(Screen.Splash.route) { inclusive = true }
									}
								}
							)
						}

						composable(Screen.Home.route) {
							HomeScreen(
								navController = navController,
							)
						}

						composable(Screen.Settings.route) {
							SettingsScreen(
								navController = navController,
								isDarkMode = viewModel.isDarkMode,
								onThemeToggle = { viewModel.toggleTheme() }
							)
						}

						composable(Screen.Map.route) {
							MapScreen()
						}
					}

					// İzin dialog'ları
					when (permissionState) {
						is NotificationPermissionState.RequiresPermission -> {
							LaunchedEffect(Unit) {
								if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
									permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
								}
							}
						}
						is NotificationPermissionState.ShowRationale -> {
							PermissionExplanationDialog(
								onConfirm = {
									permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
								},
								onDismiss = {
									// İzin reddedildi işlemleri
								}
							)
						}
						is NotificationPermissionState.Denied -> {
							PermissionDeniedDialog(
								onOpenSettings = {
									startActivity(Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
										data = Uri.fromParts("package", packageName, null)
									})
								},
								onDismiss = {
									// İzin reddedildi işlemleri
								}
							)
						}

							NotificationPermissionState.Granted -> {
							}
					}
				}
			}
		}
	}
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
	Text(
			text = "Hello $name!",
			modifier = modifier
	)
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
	MartiMapTheme {
		Greeting("Android")
	}
}