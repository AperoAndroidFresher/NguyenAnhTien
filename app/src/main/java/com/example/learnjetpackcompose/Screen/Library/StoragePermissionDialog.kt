package com.example.learnjetpackcompose.Screen.Library

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StoragePermissionDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Storage Permission", color = Color.White) },
        text = {
            Text(
                "Apero Music requires storage access to import Local music",
                color = Color.White
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK", color = Color(0xFF00C2CB),
                    style = MaterialTheme.typography.titleLarge)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Don't Allow", color = Color.White,
                    style = MaterialTheme.typography.titleLarge)
            }
        },
        containerColor = Color(0xFF292929)
    )
}

@Preview (showBackground = true)
@Composable
fun StoragePermissionDialogPreview() {
    StoragePermissionDialog(
        onDismiss = { /* Handle dismiss */ },
        onConfirm = { /* Handle confirm */ }
    )
}