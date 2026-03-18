package com.tranzo.wallet.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val FrostColorScheme = darkColorScheme(
    primary = IcyBlue,
    onPrimary = FrostWhite,
    primaryContainer = FrostSlate,
    secondary = FrostCyan,
    onSecondary = FrostBackground,
    secondaryContainer = FrostDarkSlate,
    tertiary = FrostPurple,
    background = FrostBackground,
    onBackground = FrostWhite,
    surface = FrostSurface,
    onSurface = FrostWhite,
    surfaceVariant = FrostCard,
    onSurfaceVariant = FrostGray,
    error = FrostError,
    onError = FrostWhite,
    outline = FrostBorder,
    outlineVariant = FrostSlate
)

@Composable
fun TranzoWalletTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = FrostBackground.toArgb()
            window.navigationBarColor = FrostBackground.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = FrostColorScheme,
        typography = TranzoTypography,
        content = content
    )
}
