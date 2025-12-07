package com.jithu.printerservices.ui

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.jithu.printerservices.AppOrder
import java.util.Calendar

@Composable
fun OrderEditScreen(
    order: AppOrder?,
    onSave: (AppOrder) -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    var customer by remember { mutableStateOf(order?.customer ?: "") }
    var fileName by remember { mutableStateOf(order?.fileName ?: "") }
    var description by remember { mutableStateOf(order?.description ?: "") }
    var quantity by remember { mutableStateOf(order?.quantity?.toString() ?: "1") }
    var amount by remember { mutableStateOf(order?.amount?.toString() ?: "") }
    var doubleSided by remember { mutableStateOf(order?.doubleSided ?: true) }
    var spiral by remember { mutableStateOf(order?.spiral ?: false) }
    var dueDate by remember { mutableStateOf(order?.dueDate ?: "") }
    val isEditing = order != null
    val context = LocalContext.current

    val calendar = remember { Calendar.getInstance() }

    fun showDatePicker() {
        val (year, month, day) = if (dueDate.isNotBlank()) {
            // Try to parse existing stored format yyyy-MM-dd
            try {
                val parts = dueDate.split("-")
                Triple(parts[0].toInt(), parts[1].toInt() - 1, parts[2].toInt())
            } catch (_: Exception) {
                Triple(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
            }
        } else {
            Triple(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))
        }
        DatePickerDialog(
            context,
            { _, y, m, d ->
                dueDate = String.format("%04d-%02d-%02d", y, m + 1, d)
            },
            year, month, day
        ).show()
    }

    Column(
        modifier = modifier.padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(if (isEditing) "Edit Order" else "Add New Order", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(12.dp))
        OutlinedTextField(
            value = customer,
            onValueChange = { customer = it },
            label = { Text("Customer Name") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = fileName,
            onValueChange = { fileName = it },
            label = { Text("Filename (optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = description,
            onValueChange = { description = it },
            label = { Text("Short Description (optional)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = quantity,
            onValueChange = { if (it.all(Char::isDigit)) quantity = it },
            label = { Text("Quantity") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = amount,
            onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) amount = it },
            label = { Text("Amount (₹)") },
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth()
        )
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Double-sided printing", modifier = Modifier.weight(1f))
            Switch(
                checked = doubleSided,
                onCheckedChange = { doubleSided = it },
                colors = SwitchDefaults.colors()
            )
        }
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
            Text("Spiral Binding", modifier = Modifier.weight(1f))
            Switch(checked = spiral, onCheckedChange = { spiral = it }, colors = SwitchDefaults.colors())
        }
        OutlinedTextField(
            value = if (dueDate.isNotBlank()) dueDate else "Select due date",
            onValueChange = {},
            label = { Text("Due Date") },
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker() }
        )
        Spacer(Modifier.height(20.dp))
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = onCancel) { Text("Cancel") }
            Button(
                onClick = {
                    val newOrder = AppOrder(
                        id = order?.id ?: 0L,
                        customer = customer,
                        fileName = fileName.ifBlank { null },
                        description = description.ifBlank { null },
                        quantity = quantity.toIntOrNull() ?: 1,
                        amount = amount.toDoubleOrNull() ?: 0.0,
                        doubleSided = doubleSided,
                        spiral = spiral,
                        dueDate = dueDate,
                        addedTime = order?.addedTime ?: "", // to be auto-filled on creation
                        amountReceived = order?.amountReceived ?: false,
                        completed = order?.completed ?: false
                    )
                    onSave(newOrder)
                },
                enabled = customer.isNotBlank() && quantity.isNotBlank() && amount.isNotBlank() && dueDate.isNotBlank()
            ) { Text("Save") }
        }
    }
}
