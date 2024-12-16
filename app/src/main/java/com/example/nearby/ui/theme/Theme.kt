package com.example.nearby.ui.theme

import java.util.Calendar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = GreenBase,
    onPrimary = Color.White,
    secondary = RedBase,
    onSecondary = Color.White,
    background = Gray100,
    onBackground = Gray600,
    surface = Gray200,
    onSurface = Gray500,
    error = RedBase,
    onError = Color.White,
    outline = Gray400
)

private val DarkColors = darkColorScheme(
    primary = GreenLightAlternative,
    onPrimary = GrayDarkBackground,
    secondary = RedLightAlternative,
    onSecondary = Color.Black,
    background = GrayDarkBackground,
    onBackground = GrayLightOnDark,
    surface = GrayDarkSurface,
    onSurface = GrayLightOnDark,
    error = RedDarkAlternative,
    onError = Color.Black,
    outline = Gray400
)

@Composable
fun NearbyTheme(
    content: @Composable () -> Unit
) {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val isNight = currentHour in 18..23 || currentHour in 0..5

    val colors = if (isNight) {
        DarkColors
    } else {
        LightColors
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        content = content
    )
}