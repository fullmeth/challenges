package com.mentorship.compose.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp

private val Scheme = lightColorScheme(
    primary = Accent,
    onPrimary = Bg,
    primaryContainer = Accent100,
    onPrimaryContainer = Accent800,
    secondary = Accent2,
    onSecondary = Bg,
    secondaryContainer = Accent2_100,
    onSecondaryContainer = Accent2_800,
    background = Bg,
    onBackground = TextInk,
    surface = Bg,
    onSurface = TextInk,
    surfaceVariant = Surface,
    onSurfaceVariant = Neutral700,
    surfaceContainer = Surface,
    outline = Divider,
    outlineVariant = Divider,
)

val Shapes = Shapes(
    small = RoundedCornerShape(8.dp),
    medium = RoundedCornerShape(16.dp),
    large = RoundedCornerShape(28.dp),
    extraLarge = RoundedCornerShape(32.dp),
)
val PillShape = RoundedCornerShape(percent = 50)

@Composable
fun ComposeTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = Scheme,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}