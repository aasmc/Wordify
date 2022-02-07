package ru.aasmc.wordify.resources.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = dark_primary,
    primaryVariant = dark_primaryVariant,
    secondary = dark_secondary,
    background = dark_background,
    surface = dark_surface,
    onPrimary = dark_onPrimary,
    onSecondary = dark_onSecondary,
    onBackground = dark_onBackground,
    onSurface = dark_onSurface,
    error = dark_error,
    onError = dark_onError,
    secondaryVariant = light_secondaryVariant
)

private val LightColorPalette = lightColors(
    primary = light_primary,
    primaryVariant = light_primaryVariant,
    secondary = light_secondary,
    background = light_background,
    surface = light_surface,
    onPrimary = light_onPrimary,
    onSecondary = light_onSecondary,
    onBackground = light_onBackground,
    onSurface = light_onSurface,
    error = light_error,
    onError = light_onError,
    secondaryVariant = dark_secondaryVariant
)

@Composable
fun WordifyTheme(darkTheme: Boolean, content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}