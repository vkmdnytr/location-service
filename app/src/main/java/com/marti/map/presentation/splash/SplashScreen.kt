package com.marti.map.presentation.splash

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.marti.map.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
		onSplashComplete: () -> Unit
) {
		var startAnimation by remember { mutableStateOf(false) }
		val scale = animateFloatAsState(
				targetValue = if (startAnimation) 1f else 0.5f,
				animationSpec = tween(
						durationMillis = 1000,
						easing = FastOutSlowInEasing
				),
				label = stringResource(R.string.scale)
		)

		LaunchedEffect(key1 = true) {
				startAnimation = true
				delay(2000)
				onSplashComplete()
		}

		Box(
				modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .scale(scale.value),
				contentAlignment = Alignment.Center
		) {
				Column(
						horizontalAlignment = Alignment.CenterHorizontally,
						verticalArrangement = Arrangement.Center
				) {
						Text(
								text = stringResource(R.string.marti_example),
								fontSize = 48.sp,
								fontWeight = FontWeight.Bold,
								color = MaterialTheme.colorScheme.primary
						)
						Spacer(modifier = Modifier.height(16.dp))
						Text(
								text = stringResource(R.string.welcome_to_the_future),
								fontSize = 16.sp,
								color = MaterialTheme.colorScheme.secondary
						)
				}
		}
} 