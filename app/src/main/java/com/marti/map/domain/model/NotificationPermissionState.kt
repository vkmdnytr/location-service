package com.marti.map.domain.model

sealed class NotificationPermissionState {
    object Granted : NotificationPermissionState()
    object Denied : NotificationPermissionState()
    object ShowRationale : NotificationPermissionState()
    object RequiresPermission : NotificationPermissionState()
} 