package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = KomodoMint,
    onPrimary = KomodoEmeraldDark,
    primaryContainer = KomodoTeal,
    onPrimaryContainer = KomodoTealContainer,
    secondary = KomodoAmber,
    onSecondary = KomodoAmberDark,
    secondaryContainer = Color(0xFF452205),
    onSecondaryContainer = KomodoAmberContainer,
    tertiary = KomodoCoral,
    onTertiary = Color.White,
    background = DarkBackground,
    onBackground = Color(0xFFE2E8F0),
    surface = DarkSurface,
    onSurface = Color(0xFFF1F5F9),
    surfaceVariant = DarkSurfaceCard,
    onSurfaceVariant = Color(0xFF94A3B8)
)

private val LightColorScheme = lightColorScheme(
    primary = KomodoTeal,
    onPrimary = Color.White,
    primaryContainer = KomodoTealContainer,
    onPrimaryContainer = KomodoTealDark,
    secondary = KomodoAmberDark,
    onSecondary = Color.White,
    secondaryContainer = KomodoAmberContainer,
    onSecondaryContainer = KomodoAmberDark,
    tertiary = KomodoCoral,
    onTertiary = Color.White,
    background = LightBackground,
    onBackground = Color(0xFF0F172A),
    surface = LightSurface,
    onSurface = Color(0xFF1E293B),
    surfaceVariant = Color(0xFFE2E8F0),
    onSurfaceVariant = Color(0xFF475569)
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit,
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}
