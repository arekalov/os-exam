package com.arekalov.osexam.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Gray80,
    onPrimary = Gray10,
    secondary = Gray70,
    onSecondary = Gray10,
    tertiary = Gray60,
    onTertiary = Gray10,
    background = Gray10,
    onBackground = Gray80,
    surface = Gray20,
    onSurface = Gray80,
    surfaceVariant = Gray20,
    onSurfaceVariant = Gray60,
    outline = Gray30
)

@Composable
fun OsexamTheme(
    darkTheme: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else DarkColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}