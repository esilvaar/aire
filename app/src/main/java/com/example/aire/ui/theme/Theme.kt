package com.example.aire.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

// Paleta original oscura
private val DarkColorScheme = darkColorScheme(
    primary =PrimaryGray ,
    secondary = SecondaryGray,
    tertiary = fondo1
)

// Paleta original clara
private val LightColorScheme = lightColorScheme(
    primary = PrimaryBlue,
    secondary = SecondaryBlue,
    tertiary = fondo
)


@Composable
fun AireTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
