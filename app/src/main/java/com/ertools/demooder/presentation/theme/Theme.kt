package com.ertools.demooder.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

object Theme {
    private val DarkColorPalette = darkColors(
        primary = DARK_PRIMARY,
        primaryVariant = DARK_PRIMARY,
        onPrimary = DARK_ON_PRIMARY,
        secondary = DARK_SECONDARY,
        onSecondary = DARK_ON_SECONDARY,
        background = DARK_BACKGROUND,
        onBackground = DARK_ON_BACKGROUND,
        surface = DARK_SURFACE,
        onSurface = DARK_ON_SURFACE
    )

    private val LightColorPalette = lightColors(
        primary = LIGHT_PRIMARY,
        primaryVariant = LIGHT_PRIMARY,
        onPrimary = LIGHT_ON_PRIMARY,
        secondary = LIGHT_SECONDARY,
        onSecondary = LIGHT_ON_SECONDARY,
        background = LIGHT_BACKGROUND,
        onBackground = LIGHT_ON_BACKGROUND,
        surface = LIGHT_SURFACE,
        onSurface = LIGHT_ON_SURFACE
    )

    @Composable
    fun MainTheme(
        darkTheme: Boolean = isSystemInDarkTheme(),
        content: @Composable () -> Unit
    ) {
        val colors = if (darkTheme) {
            DarkColorPalette
        } else {
            LightColorPalette
        }

        MaterialTheme(
            colors = colors,
            typography = Typography,
            shapes = Shape.Shapes,
            content = content
        )
    }
}
