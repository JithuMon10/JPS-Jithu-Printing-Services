package com.jithu.printerservices.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = PrimaryAccent,
    onPrimary = Color.White,
    primaryContainer = PrimaryAccent.copy(alpha = 0.8f),
    secondary = SecondaryAccent,
    onSecondary = Color.White,
    secondaryContainer = SecondaryAccent.copy(alpha = 0.8f),
    tertiary = PositiveGreen,
    background = DarkNeutralSurface, // Deep near-black
    onBackground = DarkTextPrimary, // High contrast off-white
    surface = DarkElevatedSurface, // Slightly lighter card surface
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkGradientSoftBlue, // Deep navy for variants
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkOutlineSoft,
    surfaceTint = PrimaryAccent.copy(alpha = 0.1f)
)

private val LightColorScheme = lightColorScheme(
    primary = PrimaryAccent,
    onPrimary = Color.White,
    primaryContainer = GradientHighlight,
    secondary = SecondaryAccent,
    onSecondary = Color.White,
    secondaryContainer = GradientSoftBlue,
    tertiary = PositiveGreen,
    background = Color(0xFFFAFAFA), // Very light gray-white
    onBackground = Color(0xFF1A1A1A), // Dark text for high contrast
    surface = Color.White, // Pure white cards
    onSurface = Color(0xFF1A1A1A), // Dark text
    surfaceVariant = Color(0xFFF5F5F5), // Light gray
    onSurfaceVariant = Color(0xFF666666), // Medium gray text
    outline = Color(0xFFE0E0E0), // Light borders
    surfaceTint = PrimaryAccent.copy(alpha = 0.05f)
)

@Composable
fun JPSTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}