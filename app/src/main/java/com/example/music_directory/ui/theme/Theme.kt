package com.example.music_directory.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// The app is always dark, like the design. No light scheme and no wallpaper-based dynamic color.
private val ColorScheme = darkColorScheme(
    primary = Accent,
    onPrimary = Color.Black,
    background = Background,
    onBackground = TextPrimary,
    surface = Background,
    onSurface = TextPrimary,
    surfaceVariant = SurfaceRaised,
    onSurfaceVariant = TextSecondary,
    surfaceContainerHigh = SurfaceRaised,
    outline = TextMuted,
)

@Composable
fun MusicdirectoryTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = ColorScheme,
        typography = Typography,
        content = content,
    )
}
