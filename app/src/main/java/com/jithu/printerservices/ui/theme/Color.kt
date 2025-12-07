package com.jithu.printerservices.ui.theme

import androidx.compose.ui.graphics.Color

// Core palette - Dark mode friendly
val IndigoPrimary = Color(0xFF6366F1)
val TealSecondary = Color(0xFF14B8A6)
val PositiveGreen = Color(0xFF22C55E)
val WarningOrange = Color(0xFFF59E0B)
val DangerRed = Color(0xFFEF4444)
val PendingBlue = Color(0xFF60A5FA)

// Dark theme gradient anchors (deep, muted)
val DarkGradientSoftBlue = Color(0xFF1E293B)
val DarkGradientSoftLavender = Color(0xFF1E1B4B)
val DarkGradientHighlight = Color(0xFF312E81)

// Light theme gradient anchors (bright, clean)
val GradientSoftBlue = Color(0xFFDBEAFE) // Lighter blue
val GradientSoftLavender = Color(0xFFF3E8FF) // Lighter lavender
val GradientHighlight = Color(0xFFE0E7FF) // Lighter indigo

// Dark theme card gradients per state (deeper tones)
val DarkPendingGradientStart = Color(0xFF1E3A8A)
val DarkPendingGradientEnd = Color(0xFF1E40AF)

val DarkReceivedGradientStart = Color(0xFF14532D)
val DarkReceivedGradientEnd = Color(0xFF166534)

val DarkCompletedUnpaidGradientStart = Color(0xFF7F1D1D)
val DarkCompletedUnpaidGradientEnd = Color(0xFF991B1B)

val DarkFullCompleteGradientStart = Color(0xFF134E4A)
val DarkFullCompleteGradientEnd = Color(0xFF1E3A8A)

// Light theme card gradients (keep existing)
val PendingGradientStart = Color(0xFFBFDBFE)
val PendingGradientEnd = Color(0xFF93C5FD)

val ReceivedGradientStart = Color(0xFFBBF7D0)
val ReceivedGradientEnd = Color(0xFF4ADE80)

val CompletedUnpaidGradientStart = Color(0xFFFECACA)
val CompletedUnpaidGradientEnd = Color(0xFFF87171)

val FullCompleteGradientStart = Color(0xFF5EEAD4)
val FullCompleteGradientEnd = Color(0xFF6366F1)

// Shared accents
val NeutralSurface = Color(0xFFF8FAFF)
val ElevatedSurface = Color(0xFFFFFFFF)
val OutlineSoft = Color(0xFFE2E8F0)
val SoftShadow = Color(0x1A000000)

// Dark theme surfaces (deep, muted)
val DarkNeutralSurface = Color(0xFF0F172A)
val DarkElevatedSurface = Color(0xFF1E293B)
val DarkOutlineSoft = Color(0xFF334155)
val DarkSoftShadow = Color(0x33000000)

// Text colors
val TextPrimary = Color(0xFF111827)
val TextSecondary = Color(0xFF334155)
val TextMuted = Color(0xFF6B7280)

// Dark theme text colors (high contrast off-white)
val DarkTextPrimary = Color(0xFFF1F5F9)
val DarkTextSecondary = Color(0xFFCBD5E1)
val DarkTextMuted = Color(0xFF94A3B8)

val PrimaryAccent = IndigoPrimary
val SecondaryAccent = TealSecondary

// Urgency colors - Dark mode versions
val OverdueRed = DangerRed
val DueTodayBlue = IndigoPrimary
val DueSoonOrange = WarningOrange

val DarkOverdueRed = Color(0xFFDC2626)
val DarkDueTodayBlue = Color(0xFF4F46E5)
val DarkDueSoonOrange = Color(0xFFEA580C)