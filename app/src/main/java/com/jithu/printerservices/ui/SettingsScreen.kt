package com.jithu.printerservices.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.input.PasswordVisualTransformation

@Composable
fun SettingsScreen(
    onChangePin: (String, String) -> Unit,
    modifier: Modifier = Modifier,
) {
    var showChangePin by remember { mutableStateOf(false) }
    var oldPin by remember { mutableStateOf("") }
    var newPin by remember { mutableStateOf("") }
    var confirmPin by remember { mutableStateOf("") }
    var pinError by remember { mutableStateOf<String?>(null) }
    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        Text("Settings", style = MaterialTheme.typography.headlineSmall)
        Button(onClick = { showChangePin = true }, modifier = Modifier.padding(vertical = 16.dp)) {
            Text("Change PIN")
        }
        Button(onClick = {}, enabled = false, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Backup/Restore (coming soon)")
        }
    }
    if (showChangePin) {
        AlertDialog(
            onDismissRequest = { showChangePin = false },
            title = { Text("Change PIN") },
            text = {
                Column {
                    OutlinedTextField(
                        value = oldPin,
                        onValueChange = { if (it.length <= 6) oldPin = it },
                        label = { Text("Current PIN") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                    OutlinedTextField(
                        value = newPin,
                        onValueChange = { if (it.length <= 6) newPin = it },
                        label = { Text("New PIN") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                    OutlinedTextField(
                        value = confirmPin,
                        onValueChange = { if (it.length <= 6) confirmPin = it },
                        label = { Text("Confirm New PIN") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                    if (pinError != null) Text(pinError!!, color = MaterialTheme.colorScheme.error)
                }
            },
            confirmButton = {
                Button(onClick = {
                    pinError = null
                    if (newPin.length != 6 || confirmPin.length != 6) {
                        pinError = "PIN must be 6 digits."
                    } else if (newPin != confirmPin) {
                        pinError = "New PINs do not match."
                    } else {
                        onChangePin(oldPin, newPin)
                        showChangePin = false
                        oldPin = ""
                        newPin = ""
                        confirmPin = ""
                    }
                }) { Text("Save") }
            },
            dismissButton = {
                Button(onClick = { showChangePin = false }) { Text("Cancel") }
            }
        )
    }
}
