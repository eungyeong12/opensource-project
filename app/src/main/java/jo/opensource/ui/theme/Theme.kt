package com.imhungry.jjongseol.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColors = lightColorScheme(
    background = md_theme_light_background,
    onBackground = md_theme_light_onBackground,
    onPrimary = md_theme_light_onPrimary
)

private val DarkColors = darkColorScheme(
    background = md_theme_dark_background,
    onBackground = md_theme_dark_onBackground,
    onPrimary = md_theme_dark_onPrimary
)

@Composable
fun AppTheme(
    useDarkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (useDarkTheme) DarkColors else LightColors

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}