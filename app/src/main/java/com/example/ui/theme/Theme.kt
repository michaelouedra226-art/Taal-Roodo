package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = GoldPrimary,
    onPrimary = Color(0xFF1F1600),
    primaryContainer = Color(0xFF3D2E00),
    onPrimaryContainer = GoldPrimary,
    secondary = AmberSecondary,
    onSecondary = Color(0xFF1F1400),
    secondaryContainer = Color(0xFF382500),
    onSecondaryContainer = AmberSecondary,
    tertiary = EmeraldSuccess,
    onTertiary = Color(0xFF00210E),
    error = CrimsonAlert,
    onError = Color.White,
    background = DarkBackground,
    onBackground = TextPrimary,
    surface = DarkSurface,
    onSurface = TextPrimary,
    surfaceVariant = DarkSurfaceElevated,
    onSurfaceVariant = TextSecondary,
    outline = DarkBorder
)

@Composable
fun TaalRoodoTheme(
    content: @Composable () -> Unit
) {
    // Dark Premium design system enforced across the app
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = Typography,
        content = content
    )
}
