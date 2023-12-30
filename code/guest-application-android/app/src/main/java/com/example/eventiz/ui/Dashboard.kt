package com.example.eventiz.ui

import android.Manifest
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.eventiz.ui.components.AnnouncementComponent
import com.example.eventiz.ui.components.EmergencyComponent
import com.example.eventiz.ui.components.RackComponent
import com.example.eventiz.ui.components.SensorRow
import com.example.eventiz.ui.screens.EventizViewModel
import kotlin.reflect.KFunction2


@OptIn(ExperimentalMaterialApi::class)
@RequiresPermission(
    anyOf = [Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION],
)
@RequiresApi(Build.VERSION_CODES.TIRAMISU)
@Composable
fun Dashboard(
    tagContent: String?,
    updateNotification: KFunction2<String, String, Unit>,
    clearNfcContent: () -> Unit
    ) {
    val viewModel: EventizViewModel = viewModel()
    val uiState by viewModel.eventizUiState.collectAsState()

    if (tagContent != null){
        viewModel.setRackNumber(tagContent)
    }

    val announcementMessage = "Announcement: ${uiState.announcementMessage}"
    val sensorInfo = "Temperature: ${uiState.temperature}°C\nCo2: ${uiState.airQuality}ppm"
    val notificationContent = "$sensorInfo\n$announcementMessage"
    updateNotification("Live Info", notificationContent)


    if (tagContent != null) {
        viewModel.setRackNumber(tagContent)
    }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = uiState.isLoading,
        onRefresh = {
            viewModel.loadAnnouncements()
            viewModel.loadInitialSensorInfo()
        }
    )
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
        PullRefreshIndicator(
            refreshing = uiState.isLoading,
            state = pullRefreshState,
            modifier = Modifier.zIndex(1f)
        )
    }
    Column(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 10.dp)
            .pullRefresh(pullRefreshState)
            .verticalScroll(rememberScrollState())
            .zIndex(-1f),

        verticalArrangement = Arrangement.Center,

        ) {
        Text(
            text = "Eventiz Dashboard",
            fontSize = 30.sp,
            modifier = Modifier.padding(bottom = 20.dp)
        )
        SensorRow(uiState = uiState, modifier = Modifier.zIndex(-1f).fillMaxWidth()
            .height(150.dp))
        Spacer(modifier = Modifier.height(30.dp))
        AnnouncementComponent(uiState = uiState)
        Spacer(modifier = Modifier.height(30.dp))
        RackComponent(uiState = uiState, viewModel = viewModel, onResetClick = clearNfcContent)
        Spacer(modifier = Modifier.height(30.dp))
        EmergencyComponent(uiState = uiState, viewModel = viewModel)
    }
}


