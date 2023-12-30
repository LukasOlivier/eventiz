package com.example.eventiz.ui.components

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventiz.model.EmergencyState
import com.example.eventiz.ui.screens.EventizUiState
import com.example.eventiz.ui.screens.EventizViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun EmergencyComponent(uiState: EventizUiState, viewModel: EventizViewModel) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
        )
        PermissionBox(
            modifier = Modifier.fillMaxSize(),
            permissions = permissions,
            requiredPermissions = listOf(permissions.first()),
        ) {
            when (uiState.emergencyState) {
                EmergencyState.SHOW_EMERGENCY_OPTIONS -> EmergencyOptions(
                    viewModel = viewModel,
                    permissions = permissions,
                    it = it
                )

                EmergencyState.EMERGENCY_SENT -> EmergencyButton(
                    eventizViewModel = viewModel,
                    text = "EMERGENCY SENT",
                )

                else -> EmergencyButton(
                    eventizViewModel = viewModel,
                    text = "EMERGENCY",
                )
            }
        }
    }
}