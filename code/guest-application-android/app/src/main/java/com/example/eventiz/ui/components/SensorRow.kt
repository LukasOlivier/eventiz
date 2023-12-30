package com.example.eventiz.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventiz.ui.screens.EventizUiState

@Composable
fun SensorRow(uiState: EventizUiState, modifier: Modifier) {
    Row(
        horizontalArrangement = Arrangement.SpaceEvenly, modifier = modifier

    ) {
        SensorComponent(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            title = "Temperature",
            unit = "°C",
            value = uiState.temperature,
            dangerousValue = uiState.dangerousTemperature
        )
        Spacer(modifier = Modifier.width(10.dp))
        SensorComponent(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            title = "Air-Quality",
            unit = "ppm",
            value = uiState.airQuality,
            dangerousValue = uiState.dangerousAirQuality
        )
    }
}