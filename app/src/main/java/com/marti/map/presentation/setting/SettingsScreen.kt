package com.marti.map.presentation.setting

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
import com.marti.map.presentation.components.ThemeSwitch
import com.marti.map.presentation.common.BottomSpace

@Composable
fun SettingsScreen(
    navController: NavController,
    isDarkMode: Boolean,
    onThemeToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(MaterialTheme.colorScheme.surface)
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.settings),
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(bottom = 24.dp)
        )
        
        ThemeSwitch(
            isDarkMode = isDarkMode,
            onThemeToggle = onThemeToggle
        )
        
        Spacer(modifier = Modifier.weight(1f))
        
        Button(
            onClick = { navController.navigateUp() }
        ) {
            Text(stringResource(R.string.back_to_home))
        }
        BottomSpace()
    }
} 