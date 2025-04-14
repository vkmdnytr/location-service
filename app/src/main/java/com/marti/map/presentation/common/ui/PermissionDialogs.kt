package com.marti.map.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable

@Composable
fun PermissionExplanationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Bildirim İzni Gerekli") },
        text = { Text("Konum takibi için bildirim izni gereklidir. Bu izin olmadan uygulama arka planda çalışamaz.") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("İzin Ver")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}

@Composable
fun PermissionDeniedDialog(
    onOpenSettings: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("İzin Gerekli") },
        text = { Text("Konum takibi için bildirim izni gereklidir. Lütfen uygulama ayarlarından bildirim iznini etkinleştirin.") },
        confirmButton = {
            TextButton(onClick = onOpenSettings) {
                Text("Ayarlara Git")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
} 