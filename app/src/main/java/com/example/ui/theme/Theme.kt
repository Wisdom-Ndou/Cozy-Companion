package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = NaturalNightSage,
    onPrimary = Color(0xFF1E281D),
    primaryContainer = Color(0xFF384636),
    onPrimaryContainer = Color(0xFFEAF0E9),
    secondary = NaturalNightClay,
    onSecondary = Color(0xFF381A1A),
    secondaryContainer = Color(0xFF5A2A2A),
    onSecondaryContainer = Color(0xFFFCEEED),
    tertiary = NaturalWarmAmber,
    background = NaturalNightBg,
    surface = NaturalNightSurface,
    surfaceVariant = NaturalNightSurfaceVariant,
    outline = NaturalNightBorder,
    outlineVariant = Color(0xFF3A352F),
    onBackground = NaturalNightText,
    onSurface = NaturalNightText,
    onSurfaceVariant = NaturalNightTextMuted,
)

private val LightColorScheme = lightColorScheme(
    primary = NaturalSage,
    onPrimary = Color.White,
    primaryContainer = NaturalSageLight,
    onPrimaryContainer = Color(0xFF2C392A),
    secondary = NaturalClay,
    onSecondary = Color.White,
    secondaryContainer = NaturalClayLight,
    onSecondaryContainer = Color(0xFF5B2323),
    tertiary = NaturalWarmAmber,
    background = NaturalBg,
    surface = NaturalSurface,
    surfaceVariant = NaturalSurfaceVariant,
    outline = NaturalBorder,
    outlineVariant = NaturalBorderLight,
    onBackground = NaturalText,
    onSurface = NaturalText,
    onSurfaceVariant = NaturalTextMuted,
)

@Composable
fun CozyCompanionTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit,
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content,
    )
}
