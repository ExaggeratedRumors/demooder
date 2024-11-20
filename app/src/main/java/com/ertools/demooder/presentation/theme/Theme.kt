package com.ertools.demooder.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

object Theme {
    private val DarkColorPalette = darkColorScheme(
        primary = Colors.PRIMARY,
        onPrimary = Colors.ON_PRIMARY,
        primaryContainer = Colors.PRIMARY_CONTAINER,
        onPrimaryContainer = Colors.ON_PRIMARY_CONTAINER,
        inversePrimary = Colors.INVERSE_PRIMARY,
        secondary = Colors.SECONDARY,
        onSecondary = Colors.ON_SECONDARY,
        secondaryContainer = Colors.SECONDARY_CONTAINER,
        onSecondaryContainer = Colors.ON_SECONDARY_CONTAINER,
        tertiary = Colors.TERTIARY,
        onTertiary = Colors.ON_TERTIARY,
        tertiaryContainer = Colors.TERTIARY_CONTAINER,
        onTertiaryContainer = Colors.ON_TERTIARY_CONTAINER,
        error = Colors.ERROR,
        onError = Colors.ON_ERROR,
        errorContainer = Colors.ERROR_CONTAINER,
        onErrorContainer = Colors.ON_ERROR_CONTAINER,
        surface = Colors.SURFACE,
        onSurface = Colors.ON_SURFACE,
        surfaceContainer = Colors.SURFACE_CONTAINER,
        surfaceContainerLow = Colors.SURFACE_CONTAINER_LOW,
        surfaceContainerLowest = Colors.SURFACE_CONTAINER_LOWEST,
        surfaceContainerHigh = Colors.SURFACE_CONTAINER_HIGH,
        surfaceContainerHighest = Colors.SURFACE_CONTAINER_HIGHEST,
        surfaceVariant = Colors.SURFACE_VARIANT,
        onSurfaceVariant = Colors.ON_SURFACE_VARIANT,
        inverseSurface = Colors.INVERSE_SURFACE,
        inverseOnSurface = Colors.INVERSE_ON_SURFACE,
        surfaceBright = Colors.SURFACE_BRIGHT,
        surfaceDim = Colors.SURFACE_DIM,
        surfaceTint = Colors.SURFACE_TINT,
        background = Colors.BACKGROUND,
        onBackground = Colors.ON_BACKGROUND,
        outline = Colors.OUTLINE,
        outlineVariant = Colors.OUTLINE_VARIANT,
    )

    private val LightColorPalette = lightColorScheme(
        primary = Colors.PRIMARY,
        onPrimary = Colors.ON_PRIMARY,
        primaryContainer = Colors.PRIMARY_CONTAINER,
        onPrimaryContainer = Colors.ON_PRIMARY_CONTAINER,
        inversePrimary = Colors.INVERSE_PRIMARY,
        secondary = Colors.SECONDARY,
        onSecondary = Colors.ON_SECONDARY,
        secondaryContainer = Colors.SECONDARY_CONTAINER,
        onSecondaryContainer = Colors.ON_SECONDARY_CONTAINER,
        tertiary = Colors.TERTIARY,
        onTertiary = Colors.ON_TERTIARY,
        tertiaryContainer = Colors.TERTIARY_CONTAINER,
        onTertiaryContainer = Colors.ON_TERTIARY_CONTAINER,
        error = Colors.ERROR,
        onError = Colors.ON_ERROR,
        errorContainer = Colors.ERROR_CONTAINER,
        onErrorContainer = Colors.ON_ERROR_CONTAINER,
        surface = Colors.SURFACE,
        onSurface = Colors.ON_SURFACE,
        surfaceContainer = Colors.SURFACE_CONTAINER,
        surfaceContainerLow = Colors.SURFACE_CONTAINER_LOW,
        surfaceContainerLowest = Colors.SURFACE_CONTAINER_LOWEST,
        surfaceContainerHigh = Colors.SURFACE_CONTAINER_HIGH,
        surfaceContainerHighest = Colors.SURFACE_CONTAINER_HIGHEST,
        surfaceVariant = Colors.SURFACE_VARIANT,
        onSurfaceVariant = Colors.ON_SURFACE_VARIANT,
        inverseSurface = Colors.INVERSE_SURFACE,
        inverseOnSurface = Colors.INVERSE_ON_SURFACE,
        surfaceBright = Colors.SURFACE_BRIGHT,
        surfaceDim = Colors.SURFACE_DIM,
        surfaceTint = Colors.SURFACE_TINT,
        background = Colors.BACKGROUND,
        onBackground = Colors.ON_BACKGROUND,
        outline = Colors.OUTLINE,
        outlineVariant = Colors.OUTLINE_VARIANT,
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
            colorScheme = colors,
            typography = Typography,
            shapes = Shape.Shapes,
            content = content
        )
    }
}
