package com.example.bity.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val customColorScheme = lightColorScheme(
    primary = Purple40,
    secondary = PurpleGrey40,
    tertiary = Pink40
)

@Composable
fun BityTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = customColorScheme,
        typography = Typography,
        content = content
    )
}
