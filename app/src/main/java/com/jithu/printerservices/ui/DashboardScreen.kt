package com.jithu.printerservices.ui

import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoFixHigh
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Layers
import androidx.compose.material.icons.filled.PendingActions
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.TaskAlt
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.jithu.printerservices.AppOrder
import com.jithu.printerservices.ui.theme.CompletedUnpaidGradientEnd
import com.jithu.printerservices.ui.theme.CompletedUnpaidGradientStart
import com.jithu.printerservices.ui.theme.FullCompleteGradientEnd
import com.jithu.printerservices.ui.theme.FullCompleteGradientStart
import com.jithu.printerservices.ui.theme.GradientHighlight
import com.jithu.printerservices.ui.theme.DarkGradientSoftBlue
import com.jithu.printerservices.ui.theme.DarkGradientSoftLavender
import com.jithu.printerservices.ui.theme.GradientSoftBlue
import com.jithu.printerservices.ui.theme.GradientSoftLavender
import com.jithu.printerservices.ui.theme.PendingGradientEnd
import com.jithu.printerservices.ui.theme.PendingGradientStart
import com.jithu.printerservices.ui.theme.PrimaryAccent
import com.jithu.printerservices.ui.theme.ReceivedGradientEnd
import com.jithu.printerservices.ui.theme.ReceivedGradientStart
import com.jithu.printerservices.ui.theme.TextMuted
import com.jithu.printerservices.ui.theme.TextPrimary
import com.jithu.printerservices.ui.theme.TextSecondary

@Composable
fun DashboardScreen(
    totalRevenue: Double,
    monthRevenue: Double,
    totalOrders: Int,
    pendingOrders: Int,
    recentOrders: List<AppOrder> = emptyList(),
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val animatedTotalRevenue by animateFloatAsState(
        targetValue = totalRevenue.toFloat(),
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
        label = "totalRevenue"
    )
    val animatedMonthRevenue by animateFloatAsState(
        targetValue = monthRevenue.toFloat(),
        animationSpec = tween(durationMillis = 600, delayMillis = 120, easing = LinearOutSlowInEasing),
        label = "monthRevenue"
    )

    Box(modifier = modifier.fillMaxSize()) {
        DecorativeBackground()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Text(
                text = "Dashboard overview",
                style = MaterialTheme.typography.headlineMedium,
                color = TextPrimary
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetricCard(
                    title = "Total revenue",
                    value = "₹${String.format("%,.2f", animatedTotalRevenue)}",
                    icon = Icons.Default.AttachMoney,
                    gradient = Brush.linearGradient(listOf(FullCompleteGradientStart, FullCompleteGradientEnd)),
                    modifier = Modifier.weight(1f)
                )
                MetricCard(
                    title = "This month",
                    value = "₹${String.format("%,.2f", animatedMonthRevenue)}",
                    icon = Icons.Default.TaskAlt,
                    gradient = Brush.linearGradient(listOf(ReceivedGradientStart, ReceivedGradientEnd))
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                MetricCard(
                    title = "Total orders",
                    value = totalOrders.toString(),
                    icon = Icons.Default.Print,
                    gradient = Brush.linearGradient(listOf(GradientSoftBlue, GradientHighlight))
                )
                MetricCard(
                    title = "Pending",
                    value = pendingOrders.toString(),
                    icon = Icons.Default.PendingActions,
                    gradient = Brush.linearGradient(listOf(PendingGradientStart, PendingGradientEnd))
                )
            }

            OutstandingHighlight(pendingOrders = pendingOrders)

            Text(
                text = "Recent orders",
                style = MaterialTheme.typography.titleLarge,
                color = TextPrimary
            )

            if (recentOrders.isEmpty()) {
                EmptyDashboardState()
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    recentOrders.take(5).forEach { order ->
                        RecentOrderCard(order = order)
                    }
                }
            }
        }
    }
}

@Composable
private fun DecorativeBackground() {
    Box(modifier = Modifier.fillMaxSize()) {
        Canvas(modifier = Modifier
            .matchParentSize()
            .padding(20.dp)) { // Reduced padding
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GradientSoftBlue, Color.Transparent),
                    center = Offset(size.width * 0.2f, size.height * 0.1f),
                    radius = size.minDimension * 0.5f // Reduced size
                )
            )
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(GradientSoftLavender, Color.Transparent),
                    center = Offset(size.width * 0.85f, size.height * 0.2f),
                    radius = size.minDimension * 0.4f // Reduced size
                )
            )
        }
    }
}

@Composable
private fun MetricCard(
    title: String,
    value: String,
    icon: ImageVector,
    gradient: Brush,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = 0.7f, stiffness = 400f),
        label = "metricScale"
    )

    Card(
        modifier = modifier
            .height(140.dp) // Reduced from 160dp
            .scale(scale),
        shape = RoundedCornerShape(20.dp), // Reduced from 26dp
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp) // Reduced from 16dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient)
                .padding(14.dp) // Reduced from 18.dp
        ) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White.copy(alpha = 0.25f),
                    modifier = Modifier.size(42.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = Color.White)
                    }
                }
                Column {
                    Text(
                        text = title.uppercase(),
                        style = MaterialTheme.typography.labelLarge,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(Modifier.height(6.dp))
                    Text(
                        text = value,
                        style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp),
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
private fun OutstandingHighlight(pendingOrders: Int) {
    val progress by animateFloatAsState(
        targetValue = if (pendingOrders == 0) 0.1f else 0.2f + (pendingOrders.coerceAtMost(10) / 10f * 0.6f),
        animationSpec = tween(durationMillis = 600, easing = LinearOutSlowInEasing),
        label = "pendingProgress"
    )

    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.6f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 18.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Brush.linearGradient(listOf(GradientSoftBlue, GradientSoftLavender)))
                .padding(vertical = 20.dp, horizontal = 24.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    Surface(shape = CircleShape, color = PrimaryAccent.copy(alpha = 0.15f)) {
                        Icon(
                            Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = PrimaryAccent,
                            modifier = Modifier.padding(10.dp)
                        )
                    }
                    Column {
                        Text(
                            text = "Pending attention",
                            style = MaterialTheme.typography.titleMedium,
                            color = TextPrimary
                        )
                        Text(
                            text = "You have $pendingOrders orders waiting for completion.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextMuted
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(Color.White.copy(alpha = 0.4f))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxHeight()
                            .fillMaxWidth(progress)
                            .clip(RoundedCornerShape(12.dp))
                            .background(PrimaryAccent.copy(alpha = 0.6f))
                    )
                }
            }
        }
    }
}

@Composable
private fun RecentOrderCard(order: AppOrder) {
    val status = remember(order.amountReceived, order.completed) {
        when {
            !order.amountReceived && !order.completed -> DashboardOrderVisual(
                label = "Pending",
                gradient = Brush.linearGradient(listOf(PendingGradientStart, PendingGradientEnd)),
                icon = Icons.Default.Schedule,
                contentColor = TextPrimary
            )

            order.amountReceived && !order.completed -> DashboardOrderVisual(
                label = "Amount received",
                gradient = Brush.linearGradient(listOf(ReceivedGradientStart, ReceivedGradientEnd)),
                icon = Icons.Default.TaskAlt,
                contentColor = TextPrimary
            )

            !order.amountReceived && order.completed -> DashboardOrderVisual(
                label = "Unpaid complete",
                gradient = Brush.linearGradient(listOf(CompletedUnpaidGradientStart, CompletedUnpaidGradientEnd)),
                icon = Icons.Default.PendingActions,
                contentColor = Color(0xFF7F1D1D)
            )

            else -> DashboardOrderVisual(
                label = "Full ✓",
                gradient = Brush.linearGradient(listOf(FullCompleteGradientStart, FullCompleteGradientEnd)),
                icon = Icons.Default.TaskAlt,
                contentColor = Color.White
            )
        }
    }

    Card(
        shape = RoundedCornerShape(28.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(status.gradient)
                .padding(horizontal = 22.dp, vertical = 18.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = order.customer,
                            style = MaterialTheme.typography.titleMedium,
                            color = status.contentColor,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        if (!order.fileName.isNullOrBlank()) {
                            Text(
                                text = order.fileName!!,
                                style = MaterialTheme.typography.bodyMedium,
                                color = status.contentColor.copy(alpha = 0.8f)
                            )
                        }
                    }
                    Text(
                        text = "₹${String.format("%,.2f", order.amount)}",
                        style = MaterialTheme.typography.titleLarge,
                        color = status.contentColor
                    )
                }

                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    DashboardChip(icon = Icons.Default.Layers, text = if (order.doubleSided) "Double sided" else "Single sided", contentColor = status.contentColor)
                    DashboardChip(icon = Icons.Default.Print, text = "Qty ${order.quantity}", contentColor = status.contentColor)
                    if (order.spiral) {
                        DashboardChip(icon = Icons.Default.AutoFixHigh, text = "Spiral", contentColor = status.contentColor)
                    }
                }

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    DashboardChip(icon = Icons.Default.Schedule, text = "Due ${order.dueDate}", contentColor = status.contentColor)
                    DashboardChip(icon = status.icon, text = status.label, contentColor = status.contentColor)
                }
            }
        }
    }
}

private data class DashboardOrderVisual(
    val label: String,
    val gradient: Brush,
    val icon: ImageVector,
    val contentColor: Color
)

@Composable
private fun DashboardChip(icon: ImageVector, text: String, contentColor: Color) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White.copy(alpha = 0.18f),
        contentColor = contentColor,
        tonalElevation = 0.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(16.dp))
            Text(text, style = MaterialTheme.typography.labelLarge)
        }
    }
}

@Composable
private fun EmptyDashboardState() {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        tonalElevation = 2.dp,
        color = Color.White.copy(alpha = 0.7f)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 28.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(shape = CircleShape, color = PrimaryAccent.copy(alpha = 0.12f)) {
                Icon(
                    Icons.Default.AutoFixHigh,
                    contentDescription = null,
                    tint = PrimaryAccent,
                    modifier = Modifier.padding(12.dp)
                )
            }
            Text(
                text = "No orders yet",
                style = MaterialTheme.typography.titleMedium,
                color = TextPrimary
            )
            Text(
                text = "Create a new order to see it appear here.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }
    }
}
