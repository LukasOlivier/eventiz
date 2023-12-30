package com.example.eventiz.ui.components

import android.Manifest
import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.eventiz.model.EmergencyType
import com.example.eventiz.ui.screens.EventizViewModel

@SuppressLint("MissingPermission")
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun EmergencyOptions(viewModel: EventizViewModel, permissions: List<String>, it: List<String>) {
    LazyRow(
        horizontalArrangement = Arrangement.SpaceEvenly,
        modifier = Modifier.fillMaxSize()
    ) {
        item {
            EmergencyTypeButton(
                usePreciseLocation = false,
                eventizViewModel = viewModel,
                emergencyType = EmergencyType.CANCEL
            )
        }
        item {
            Spacer(modifier = Modifier.width(10.dp))
        }
        item {
            EmergencyTypeButton(
                usePreciseLocation = it.contains(Manifest.permission.ACCESS_FINE_LOCATION),
                eventizViewModel = viewModel,
                emergencyType = EmergencyType.FIRE
            )
        }
        item {
            Spacer(modifier = Modifier.width(10.dp))
        }
        item {
            EmergencyTypeButton(
                usePreciseLocation = it.contains(Manifest.permission.ACCESS_FINE_LOCATION),
                eventizViewModel = viewModel,
                emergencyType = EmergencyType.MEDICAL
            )
        }
        item {
            Spacer(modifier = Modifier.width(10.dp))
        }
        item {
            EmergencyTypeButton(
                usePreciseLocation = it.contains(Manifest.permission.ACCESS_FINE_LOCATION),
                eventizViewModel = viewModel,
                emergencyType = EmergencyType.VIOLENCE
            )
        }
        item {
            Spacer(modifier = Modifier.width(10.dp))
        }
        item {
            EmergencyTypeButton(
                usePreciseLocation = it.contains(Manifest.permission.ACCESS_FINE_LOCATION),
                eventizViewModel = viewModel,
                emergencyType = EmergencyType.OTHER
            )
        }
    }
}