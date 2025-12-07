package com.jithu.printerservices.ui

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jithu.printerservices.AppOrder
import com.jithu.printerservices.ui.theme.DarkPendingGradientStart
import com.jithu.printerservices.ui.theme.DarkPendingGradientEnd
import com.jithu.printerservices.ui.theme.DarkReceivedGradientStart
import com.jithu.printerservices.ui.theme.DarkReceivedGradientEnd
import com.jithu.printerservices.ui.theme.DarkCompletedUnpaidGradientStart
import com.jithu.printerservices.ui.theme.DarkCompletedUnpaidGradientEnd
import com.jithu.printerservices.ui.theme.DarkFullCompleteGradientStart
import com.jithu.printerservices.ui.theme.DarkFullCompleteGradientEnd
import com.jithu.printerservices.ui.theme.DarkTextPrimary
import com.jithu.printerservices.ui.theme.DarkTextSecondary
import com.jithu.printerservices.ui.theme.DarkTextMuted
import com.jithu.printerservices.ui.theme.CompletedUnpaidGradientStart
import com.jithu.printerservices.ui.theme.CompletedUnpaidGradientEnd
import com.jithu.printerservices.ui.theme.FullCompleteGradientStart
import com.jithu.printerservices.ui.theme.FullCompleteGradientEnd
import com.jithu.printerservices.ui.theme.DangerRed
import com.jithu.printerservices.ui.theme.OutlineSoft
import com.jithu.printerservices.ui.theme.PendingBlue
import com.jithu.printerservices.ui.theme.PendingGradientEnd
import com.jithu.printerservices.ui.theme.PendingGradientStart
import com.jithu.printerservices.ui.theme.PositiveGreen
import com.jithu.printerservices.ui.theme.PrimaryAccent
import com.jithu.printerservices.ui.theme.ReceivedGradientEnd
import com.jithu.printerservices.ui.theme.ReceivedGradientStart
import com.jithu.printerservices.ui.theme.SecondaryAccent
import com.jithu.printerservices.ui.theme.TextMuted
import com.jithu.printerservices.ui.theme.TextPrimary
import com.jithu.printerservices.ui.theme.TextSecondary
import com.jithu.printerservices.ui.theme.OverdueRed
import com.jithu.printerservices.ui.theme.DueTodayBlue
import com.jithu.printerservices.ui.theme.DueSoonOrange
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.widget.Toast

@Composable
fun OrderListScreen(
    orders: List<AppOrder>,
    onToggleAmountReceived: (AppOrder) -> Unit,
    onToggleCompleted: (AppOrder) -> Unit,
    onEditOrder: (AppOrder) -> Unit,
    onDeleteOrder: (AppOrder) -> Unit,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    val context = LocalContext.current
    var pendingToggleOrder by remember { mutableStateOf<AppOrder?>(null) }
    var pendingToggleType by remember { mutableStateOf<ToggleType?>(null) }
    var pendingNewState by remember { mutableStateOf<Boolean?>(null) }

    // Automatic sorting: pending/unpaid first, then completed orders
    val sortedOrders = remember(orders, searchQuery) {
        orders.filter {
            it.customer.contains(searchQuery, true) ||
                    (it.fileName?.contains(searchQuery, true) == true)
        }.sortedWith(compareBy<AppOrder> { 
            // Group by status: pending/unpaid (0), completed (1)
            when {
                !it.completed && !it.amountReceived -> 0
                !it.completed && it.amountReceived -> 0
                it.completed && !it.amountReceived -> 0
                else -> 1 // fully completed
            }
        }.thenByDescending { 
            // Within each group, sort by added time (newest first)
            try {
                java.time.LocalDateTime.parse(it.addedTime)
            } catch (e: Exception) {
                java.time.LocalDateTime.MIN
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 4.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Spacer(modifier = Modifier.height(8.dp))

        if (sortedOrders.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                ElevatedCard(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Icon(
                            Icons.Default.Inbox,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(32.dp)
                        )
                        Text(
                            text = "You're all caught up!",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "Tap the + button to schedule your first print job.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        } else {
            androidx.compose.foundation.lazy.LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                itemsIndexed(sortedOrders, key = { _, order -> order.id }) { index, order ->
                    AnimatedOrderCard(
                        order = order,
                        index = index,
                        onEditOrder = { onEditOrder(order) },
                        onToggleAmountReceived = {
                            pendingToggleOrder = order
                            pendingToggleType = ToggleType.AMOUNT
                            pendingNewState = !order.amountReceived
                        },
                        onToggleCompleted = {
                            pendingToggleOrder = order
                            pendingToggleType = ToggleType.COMPLETED
                            pendingNewState = !order.completed
                        },
                        onDeleteOrder = { onDeleteOrder(order) }
                    )
                }
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Created by JithuMon",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                        )
                    }
                }
            }
        }
    }

    // Confirmation dialog for toggles
    if (pendingToggleOrder != null && pendingToggleType != null && pendingNewState != null) {
        val order = pendingToggleOrder!!
        val isMarking = pendingNewState == true
        val message = when (pendingToggleType) {
            ToggleType.AMOUNT -> if (isMarking) "Mark amount as received for this order?" else "Unmark amount as received?"
            ToggleType.COMPLETED -> if (isMarking) "Mark this order as completed?" else "Unmark this order as completed?"
            else -> ""
        }
        AlertDialog(
            onDismissRequest = {
                pendingToggleOrder = null
                pendingToggleType = null
                pendingNewState = null
            },
            title = { Text("Confirm") },
            text = { Text(message) },
            confirmButton = {
                Button(
                    onClick = {
                        when (pendingToggleType) {
                            ToggleType.AMOUNT -> {
                                onToggleAmountReceived(order)
                                Toast.makeText(
                                    context,
                                    if (isMarking) "Amount marked received" else "Amount unmarked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            ToggleType.COMPLETED -> {
                                onToggleCompleted(order)
                                Toast.makeText(
                                    context,
                                    if (isMarking) "Order marked completed" else "Order unmarked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                            else -> {}
                        }
                        pendingToggleOrder = null
                        pendingToggleType = null
                        pendingNewState = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryAccent)
                ) {
                    Text("Confirm")
                }
            },
            dismissButton = {
                OutlinedButton(
                    onClick = {
                        pendingToggleOrder = null
                        pendingToggleType = null
                        pendingNewState = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Composable
fun AnimatedOrderCard(
    order: AppOrder,
    index: Int,
    onEditOrder: () -> Unit,
    onToggleAmountReceived: () -> Unit,
    onToggleCompleted: () -> Unit,
    onDeleteOrder: () -> Unit
) {
    val cardShape = RoundedCornerShape(20.dp) // Reduced corner radius
    val isDark = MaterialTheme.colorScheme.background == Color(0xFF0F172A) // Check if dark mode
    val stateVisual = remember(order.amountReceived, order.completed) {
        when {
            !order.amountReceived && !order.completed -> CardVisual(
                gradient = Brush.linearGradient(
                    listOf(
                        if (isDark) DarkPendingGradientStart else PendingGradientStart,
                        if (isDark) DarkPendingGradientEnd else PendingGradientEnd
                    )
                ),
                border = BorderStroke(1.2.dp, PendingBlue.copy(alpha = 0.6f)),
                badgeLabel = "Pending",
                badgeColor = PendingBlue,
                badgeIcon = Icons.Default.Schedule,
                badgeTextColor = if (isDark) DarkTextPrimary else TextPrimary,
                contentColor = if (isDark) DarkTextPrimary else TextPrimary,
                showShimmer = false
            )

            order.amountReceived && !order.completed -> CardVisual(
                gradient = Brush.linearGradient(
                    listOf(
                        if (isDark) DarkReceivedGradientStart else ReceivedGradientStart,
                        if (isDark) DarkReceivedGradientEnd else ReceivedGradientEnd
                    )
                ),
                border = null,
                badgeLabel = "Amount received",
                badgeColor = PositiveGreen,
                badgeIcon = Icons.Default.AttachMoney,
                badgeTextColor = if (isDark) DarkTextPrimary else TextPrimary,
                contentColor = if (isDark) DarkTextPrimary else TextPrimary,
                showShimmer = false
            )

            !order.amountReceived && order.completed -> CardVisual(
                gradient = Brush.linearGradient(
                    listOf(
                        if (isDark) DarkCompletedUnpaidGradientStart else CompletedUnpaidGradientStart,
                        if (isDark) DarkCompletedUnpaidGradientEnd else CompletedUnpaidGradientEnd
                    )
                ),
                border = null,
                badgeLabel = "Unpaid complete",
                badgeColor = DangerRed,
                badgeIcon = Icons.Default.Warning,
                badgeTextColor = Color.White,
                contentColor = Color(0xFF7F1D1D),
                showShimmer = false
            )

            else -> CardVisual(
                gradient = Brush.linearGradient(
                    listOf(
                        if (isDark) DarkFullCompleteGradientStart else FullCompleteGradientStart,
                        if (isDark) DarkFullCompleteGradientEnd else FullCompleteGradientEnd
                    )
                ),
                border = null,
                badgeLabel = "FULL ✓",
                badgeColor = Color.White,
                badgeIcon = Icons.Default.CheckCircle,
                badgeTextColor = Color(0xFF0F172A),
                contentColor = Color.White,
                showShimmer = true
            )
        }
    }

    val fadeAlpha by animateFloatAsState(
        targetValue = 1f,
        animationSpec = tween(durationMillis = 200, delayMillis = index * 20), // Reduced duration and delay
        label = "fadeIn"
    )
    val pulseScale by animateFloatAsState(
        targetValue = if (order.amountReceived && !order.completed) 1.01f else 1f, // Reduced scale
        animationSpec = tween(durationMillis = 300, easing = LinearEasing), // Simplified easing
        label = "pulse"
    )

    val (dueDateColor, dueDateIcon) = getDueDateUrgency(order.dueDate, MaterialTheme.colorScheme, order.completed && order.amountReceived)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .alpha(fadeAlpha)
            .scale(pulseScale)
            .clickable { onEditOrder() },
        shape = cardShape,
        tonalElevation = 4.dp, // Reduced from 8dp
        shadowElevation = 6.dp, // Reduced from 12dp
        color = Color.Transparent
    ) {
        Box(
            modifier = Modifier
                .background(stateVisual.gradient, cardShape)
                .then(
                    if (stateVisual.border != null) Modifier.border(stateVisual.border, cardShape)
                    else Modifier
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 18.dp, vertical = 16.dp), // Reduced from 22dp, 20dp
                verticalArrangement = Arrangement.spacedBy(10.dp) // Reduced from 14dp
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = order.customer,
                            style = MaterialTheme.typography.titleLarge,
                            color = stateVisual.contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!order.fileName.isNullOrBlank() || !order.description.isNullOrBlank()) {
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = order.fileName?.takeIf { it.isNotBlank() }
                                    ?: order.description.orEmpty(),
                                style = MaterialTheme.typography.bodyMedium,
                                color = stateVisual.contentColor.copy(alpha = 0.8f),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        InfoChip(
                            icon = Icons.Default.FormatListNumbered,
                            text = "Qty ${order.quantity}",
                            containerColor = Color.White.copy(alpha = 0.25f),
                            contentColor = stateVisual.contentColor
                        )
                        Spacer(Modifier.height(8.dp))
                        InfoChip(
                            icon = Icons.Default.Payments,
                            text = "₹${String.format("%.2f", order.amount)}",
                            containerColor = Color.White.copy(alpha = 0.25f),
                            contentColor = stateVisual.contentColor
                        )
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    InfoChip(
                        icon = if (order.doubleSided) Icons.Default.Layers else Icons.Default.FileCopy,
                        text = if (order.doubleSided) "Double sided" else "Single sided",
                        containerColor = Color.White.copy(alpha = 0.2f),
                        contentColor = stateVisual.contentColor
                    )
                    if (order.spiral) {
                        InfoChip(
                            icon = Icons.Default.AutoFixHigh,
                            text = "Spiral binding",
                            containerColor = Color.White.copy(alpha = 0.2f),
                            contentColor = stateVisual.contentColor
                        )
                    }
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (dueDateIcon != null) {
                        Icon(
                            dueDateIcon,
                            contentDescription = null,
                            tint = dueDateColor,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(6.dp))
                    }
                    Text(
                        text = "Due ${order.dueDate}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = dueDateColor
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Added ${formatAdded(order.addedTime)}",
                        style = MaterialTheme.typography.bodySmall,
                        color = stateVisual.contentColor.copy(alpha = 0.7f)
                    )
                }

                StateBadge(
                    icon = stateVisual.badgeIcon,
                    label = stateVisual.badgeLabel,
                    containerColor = stateVisual.badgeColor,
                    contentColor = stateVisual.badgeTextColor
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    StateToggle(
                        label = "Amount received",
                        checked = order.amountReceived,
                        activeColor = PositiveGreen,
                        onToggle = onToggleAmountReceived
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    StateToggle(
                        label = "Completed",
                        checked = order.completed,
                        activeColor = SecondaryAccent,
                        onToggle = onToggleCompleted
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    ActionIconButton(
                        icon = Icons.Default.Edit,
                        containerColor = Color.White.copy(alpha = 0.2f),
                        contentDescription = "Edit order",
                        onClick = onEditOrder,
                        contentColor = stateVisual.contentColor
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    ActionIconButton(
                        icon = Icons.Default.Delete,
                        containerColor = Color.White.copy(alpha = 0.2f),
                        contentDescription = "Delete order",
                        onClick = onDeleteOrder,
                        contentColor = DangerRed
                    )
                }
            }
        }
    }
}

private data class CardVisual(
    val gradient: Brush,
    val border: BorderStroke?,
    val badgeLabel: String,
    val badgeColor: Color,
    val badgeIcon: ImageVector,
    val badgeTextColor: Color,
    val contentColor: Color,
    val showShimmer: Boolean
)

@Composable
fun InfoChip(
    icon: ImageVector,
    text: String,
    containerColor: Color,
    contentColor: Color
) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun StateBadge(icon: ImageVector, label: String, containerColor: Color, contentColor: Color) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = containerColor,
        contentColor = contentColor,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Text(label.uppercase(), style = MaterialTheme.typography.labelSmall)
        }
    }
}

@Composable
private fun StateToggle(
    label: String,
    checked: Boolean,
    activeColor: Color,
    onToggle: () -> Unit
) {
    val background by animateColorAsState(
        targetValue = if (checked) Color.White.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.08f),
        animationSpec = tween(durationMillis = 200),
        label = "toggleBackground"
    )
    val indicator by animateColorAsState(
        targetValue = if (checked) activeColor else TextMuted,
        label = "toggleIndicator"
    )

    Surface(
        modifier = Modifier
            .height(44.dp)
            .clip(RoundedCornerShape(20.dp))
            .clickable { onToggle() },
        color = background,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
                    .background(indicator)
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelLarge,
                color = indicator
            )
        }
    }
}

@Composable
private fun ActionIconButton(
    icon: ImageVector,
    containerColor: Color,
    contentColor: Color,
    contentDescription: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier.size(42.dp),
        shape = CircleShape,
        color = containerColor,
        tonalElevation = 0.dp
    ) {
        IconButton(onClick = onClick, modifier = Modifier.fillMaxSize()) {
            Icon(icon, contentDescription = contentDescription, tint = contentColor)
        }
    }
}

private enum class ToggleType { AMOUNT, COMPLETED }

private fun formatAdded(raw: String): String {
    return try {
        val dt = LocalDateTime.parse(raw)
        val today = LocalDate.now()
        val date = dt.toLocalDate()
        val timePart = dt.format(DateTimeFormatter.ofPattern("h:mm a"))
        when {
            date == today -> "Today, $timePart"
            date == today.minusDays(1) -> "Yesterday, $timePart"
            else -> dt.format(DateTimeFormatter.ofPattern("dd MMM yyyy, h:mm a"))
        }
    } catch (e: Exception) {
        raw
    }
}

@Composable
private fun getDueDateUrgency(dueDateStr: String, colorScheme: androidx.compose.material3.ColorScheme, isCompletedWithAmount: Boolean): Pair<Color, androidx.compose.ui.graphics.vector.ImageVector?> {
    return try {
        val parts = dueDateStr.split("-")
        val dueDate = LocalDate.of(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
        val today = LocalDate.now()
        val daysUntil = java.time.temporal.ChronoUnit.DAYS.between(today, dueDate)
        when {
            isCompletedWithAmount -> Pair(colorScheme.onSurfaceVariant, null) // Normal date for completed orders
            daysUntil < 0 -> Pair(OverdueRed, Icons.Default.Warning)
            daysUntil == 0L -> Pair(DueTodayBlue, null)
            daysUntil <= 3 -> Pair(DueSoonOrange, null)
            else -> Pair(colorScheme.onSurfaceVariant, null)
        }
    } catch (e: Exception) {
        Pair(colorScheme.onSurfaceVariant, null)
    }
}
