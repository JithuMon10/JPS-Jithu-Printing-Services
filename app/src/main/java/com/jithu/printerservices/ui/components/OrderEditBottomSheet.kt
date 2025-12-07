package com.jithu.printerservices.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jithu.printerservices.AppOrder
import com.jithu.printerservices.ui.theme.GradientSoftBlue
import com.jithu.printerservices.ui.theme.GradientSoftLavender
import com.jithu.printerservices.ui.theme.PrimaryAccent
import com.jithu.printerservices.ui.theme.TextMuted
import com.jithu.printerservices.ui.theme.TextPrimary
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderEditBottomSheet(
    order: AppOrder?,
    onSave: (AppOrder) -> Unit,
    onDismiss: () -> Unit,
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
    var showCalendarDialog by remember { mutableStateOf(false) }
    val isEditing = order != null

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    
    // Parse ISO date string to LocalDate for calendar
    fun parseDate(dateStr: String): LocalDate? {
        return try {
            if (dateStr.isNotBlank()) {
                LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
            } else null
        } catch (e: Exception) {
            null
        }
    }
    
    // Format LocalDate to friendly display string (e.g., "04 Dec 2025")
    fun formatDateForDisplay(dateStr: String): String {
        return try {
            if (dateStr.isNotBlank()) {
                val date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
                date.format(DateTimeFormatter.ofPattern("dd MMM yyyy"))
            } else ""
        } catch (e: Exception) {
            ""
        }
    }
    
    // Get initial date for calendar (existing date or today)
    val initialDate = remember(dueDate) {
        parseDate(dueDate) ?: LocalDate.now()
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
        containerColor = Color.Transparent,
        dragHandle = {},
        modifier = modifier
    ) {
        ElevatedCard(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 4.dp),
            shape = RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp),
            colors = androidx.compose.material3.CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Box {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .clip(RoundedCornerShape(topStart = 32.dp, topEnd = 32.dp))
                        .background(
                            brush = Brush.linearGradient(
                                colors = listOf(GradientSoftBlue, GradientSoftLavender)
                            )
                        )
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .width(48.dp)
                            .height(5.dp)
                            .clip(RoundedCornerShape(50))
                            .background(Color.White.copy(alpha = 0.6f))
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = if (isEditing) "Edit order" else "Create new order",
                        style = MaterialTheme.typography.headlineMedium,
                        color = TextPrimary
                    )
                    Text(
                        text = "Enter order details and scheduling information.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextMuted
                    )
                }
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp)
            ) {
                TextField(
                    value = customer,
                    onValueChange = { customer = it },
                    label = { Text("Customer name") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = PrimaryAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = fileName,
                    onValueChange = { fileName = it },
                    label = { Text("Filename (optional)") },
                    singleLine = true,
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = PrimaryAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                TextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Short description (optional)") },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = PrimaryAccent
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    TextField(
                        value = quantity,
                        onValueChange = { if (it.all(Char::isDigit)) quantity = it },
                        label = { Text("Quantity") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = PrimaryAccent
                        ),
                        modifier = Modifier.weight(1f)
                    )
                    TextField(
                        value = amount,
                        onValueChange = { if (it.isEmpty() || it.toDoubleOrNull() != null) amount = it },
                        label = { Text("Amount (₹)") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                        colors = TextFieldDefaults.colors(
                            focusedContainerColor = MaterialTheme.colorScheme.surface,
                            unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                            focusedIndicatorColor = Color.Transparent,
                            unfocusedIndicatorColor = Color.Transparent,
                            cursorColor = PrimaryAccent
                        ),
                        modifier = Modifier.weight(1f)
                    )
                }

                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(
                        text = "Print settings",
                        style = MaterialTheme.typography.titleMedium,
                        color = TextPrimary
                    )
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        PrintTypeChip(
                            selected = doubleSided,
                            label = "Double sided",
                            onClick = { doubleSided = true }
                        )
                        PrintTypeChip(
                            selected = !doubleSided,
                            label = "Single sided",
                            onClick = { doubleSided = false }
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Spiral binding",
                            style = MaterialTheme.typography.bodyLarge,
                            color = TextMuted,
                            modifier = Modifier.weight(1f)
                        )
                        Switch(
                            checked = spiral,
                            onCheckedChange = { spiral = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = PrimaryAccent,
                                checkedTrackColor = PrimaryAccent.copy(alpha = 0.35f)
                            )
                        )
                    }
                }

                val dateDisplayText = formatDateForDisplay(dueDate)
                TextField(
                    value = dateDisplayText,
                    onValueChange = {},
                    label = { Text("Due date") },
                    placeholder = { Text("Select due date") },
                    readOnly = true,
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = { showCalendarDialog = true }) {
                            Icon(
                                Icons.Default.CalendarToday,
                                contentDescription = "Select date",
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = MaterialTheme.colorScheme.surface,
                        unfocusedContainerColor = MaterialTheme.colorScheme.surface,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = PrimaryAccent
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            indication = null,
                            interactionSource = remember { MutableInteractionSource() }
                        ) {
                            showCalendarDialog = true
                        }
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Button(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Transparent,
                            contentColor = TextPrimary
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.2.dp, PrimaryAccent.copy(alpha = 0.4f))
                    ) {
                        Text("Cancel", fontWeight = FontWeight.Medium)
                    }
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
                                addedTime = order?.addedTime ?: "",
                                amountReceived = order?.amountReceived ?: false,
                                completed = order?.completed ?: false
                            )
                            onSave(newOrder)
                        },
                        enabled = customer.isNotBlank() && quantity.isNotBlank() && amount.isNotBlank() && dueDate.isNotBlank(),
                        modifier = Modifier.weight(1f),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = PrimaryAccent,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Save order", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }

    // Calendar picker dialog
    CalendarPickerDialog(
        visible = showCalendarDialog,
        initialDate = initialDate,
        onDateSelected = { date ->
            // Store as ISO date (YYYY-MM-DD)
            dueDate = date.format(DateTimeFormatter.ISO_DATE)
            showCalendarDialog = false
        },
        onDismiss = {
            showCalendarDialog = false
        }
    )
}

@Composable
private fun PrintTypeChip(selected: Boolean, label: String, onClick: () -> Unit) {
    val highlight by animateFloatAsState(
        targetValue = if (selected) 1f else 0f,
        animationSpec = tween(durationMillis = 220),
        label = "printTypeChip"
    )
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(22.dp),
        color = Color.White.copy(alpha = 0.08f + 0.25f * highlight),
        tonalElevation = 0.dp,
        border = if (selected) null else BorderStroke(1.dp, PrimaryAccent.copy(alpha = 0.35f))
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (selected) {
                Surface(shape = CircleShape, color = PrimaryAccent.copy(alpha = 0.2f)) {
                    Icon(
                        Icons.Default.Check,
                        contentDescription = null,
                        tint = PrimaryAccent,
                        modifier = Modifier.padding(4.dp)
                    )
                }
            } else {
                Spacer(modifier = Modifier.size(24.dp))
            }
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = if (selected) PrimaryAccent else TextMuted
            )
        }
    }
}

