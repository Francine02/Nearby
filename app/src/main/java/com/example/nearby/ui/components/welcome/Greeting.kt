package com.example.nearby.ui.components.welcome

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.example.nearby.ui.theme.Typography
import java.util.Calendar

@Composable
fun GreetingBasedOnTime() {
    val currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
    val (greeting, emoji) = when {
        currentHour in 6..11 -> "Bom dia" to "☀️"
        currentHour in 12..17 -> "Boa tarde" to "🌞"
        else -> "Boa noite" to "🌙"
    }

    Text(text = "$greeting $emoji", style = Typography.headlineSmall)
}