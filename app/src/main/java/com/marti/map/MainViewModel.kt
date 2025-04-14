package com.marti.map

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.marti.map.domain.model.NotificationPermissionState
import com.marti.map.utils.PreferenceManager
import com.marti.map.utils.PermissionHelper
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val  preferenceManager: PreferenceManager,
    private val permissionHelper: PermissionHelper
) : ViewModel() {

    private val _permissionState = MutableStateFlow<NotificationPermissionState>(
        NotificationPermissionState.RequiresPermission
    )
    val permissionState: StateFlow<NotificationPermissionState> = _permissionState.asStateFlow()

    var isDarkMode by mutableStateOf(loadDarkModeState())
        private set
    
    private fun loadDarkModeState(): Boolean {
        return preferenceManager.getDarkModeState()
    }
    
    private fun saveDarkModeState(isDark: Boolean) {
        preferenceManager.saveDarkModeState(isDark)
    }
    
    fun toggleTheme() {
        isDarkMode = !isDarkMode
        saveDarkModeState(isDarkMode)
    }

    init {
        checkNotificationPermission()
    }

    private fun checkNotificationPermission() {
        val hasPermission = permissionHelper.checkNotificationPermission()
        _permissionState.value = if (hasPermission) {
            NotificationPermissionState.Granted
        } else {
            NotificationPermissionState.RequiresPermission
        }
    }

    fun onPermissionResult(isGranted: Boolean, shouldShowRationale: Boolean) {
        _permissionState.value = when {
            isGranted -> NotificationPermissionState.Granted
            shouldShowRationale -> NotificationPermissionState.ShowRationale
            else -> NotificationPermissionState.Denied
        }
    }
}