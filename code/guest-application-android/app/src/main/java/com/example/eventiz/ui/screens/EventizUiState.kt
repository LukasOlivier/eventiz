package com.example.eventiz.ui.screens

import com.example.eventiz.model.EmergencyState


data class EventizUiState(
    val emergencyState: EmergencyState = EmergencyState.DEFAULT,
    val rackText: String? = null,
    val nfcEnabled : Boolean? = null,
    val announcementMessage: String = "No announcements yet",
    val announcementCategory: String = "None",
    val temperature: Float = 0f,
    val airQuality: Float = 0f,
    val isLoading : Boolean = false,
    val dangerousTemperature: Boolean = false,
    val dangerousAirQuality: Boolean = false,
)