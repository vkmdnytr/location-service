package com.marti.map.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.marti.map.R
import com.marti.map.navigation.Screen
import com.marti.map.presentation.common.BottomSpace

@Composable
fun HomeScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.home_screen),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        // Map Button
        Button(
            onClick = { navController.navigate(Screen.Map.route) },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
        ) {
            Text(stringResource(R.string.open_map))
        }
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Button(
            onClick = { navController.navigate(Screen.Settings.route) }
        ) {
            Text(stringResource(R.string.go_to_settings))
        }
        BottomSpace()
    }
} 