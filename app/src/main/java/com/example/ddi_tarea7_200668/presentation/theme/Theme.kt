package com.example.ddi_tarea7_200668.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.MaterialTheme

@Composable
fun DDI_Tarea7_200668Theme(
        content: @Composable () -> Unit
) {
    MaterialTheme(
            colors = wearColorPalette,
            typography = Typography,
            // For shapes, we generally recommend using the default Material Wear shapes which are
            // optimized for round and non-round devices.
            content = content
    )
}