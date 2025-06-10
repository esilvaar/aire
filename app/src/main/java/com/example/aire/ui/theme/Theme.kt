package com.example.aire.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

// Paleta original oscura
private val DarkColorScheme = darkColorScheme(
    primary = TertiaryGrey,
    secondary = PrimaryGray,
    tertiary = SecondaryGrey
)

// Paleta original clara
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryBlue,
    tertiary = TertiaryBlue
)


@Composable
fun AireTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    useGreenPalette: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        !useGreenPalette && darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
