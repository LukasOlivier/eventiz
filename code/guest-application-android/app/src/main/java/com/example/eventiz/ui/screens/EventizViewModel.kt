package com.example.eventiz.ui.screens

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.eventiz.R
import com.example.eventiz.model.Emergency
import com.example.eventiz.model.EmergencyState
import com.example.eventiz.network.EventizApi
import com.example.eventiz.network.EventizCms
import com.example.eventiz.network.Websockets
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import java.io.IOException
import java.net.SocketTimeoutException


@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class EventizViewModel(application: Application) : AndroidViewModel(application) {
    val enableNfcText = "Tap to enable NFC"
    val defaultRackText = "Scan bracelet to see rack number"
    val noNfcRackText = "This device doesn't support NFC."
    private val _eventUiState = MutableStateFlow(EventizUiState())
    val eventizUiState: StateFlow<EventizUiState> = _eventUiState.asStateFlow()
    @Suppress("unused") // the Websocket class needs to be initialized to start listening for events
    val ws = Websockets(this)

    init {
        if (checkNetworkConnectivity()) {
            schedulePeriodicAnnouncementFetch()
            loadInitialSensorInfo()
        }
    }


    // Announcements
    fun loadAnnouncements() {
        viewModelScope.launch {
            try {
                val announcements = EventizCms.retrofitService.getAnnouncements()
                val latestAnnouncement = announcements.data[announcements.data.size - 1].attributes
                _eventUiState.value = _eventUiState.value.copy(
                    announcementMessage = latestAnnouncement.message,
                    announcementCategory = latestAnnouncement.category.data.attributes.type
                )
            } catch (e: IOException) {
                _eventUiState.value = _eventUiState.value.copy(
                    announcementMessage = "No connection",
                    announcementCategory = "None"
                )
            } catch (e: SocketTimeoutException) {
                _eventUiState.value = _eventUiState.value.copy(
                    announcementMessage = "No connection",
                    announcementCategory = "None"
                )
            }
        }
    }
    private fun schedulePeriodicAnnouncementFetch() {
        val periodicJob = viewModelScope.launch {
            while (true) {
                loadAnnouncements()
                delay(10000)
            }
        }
        periodicJob.start()
    }


    // Sensors
        //HTTP
         fun loadInitialSensorInfo() {
        viewModelScope.launch {
            try {
                val sensors = EventizApi.retrofitService.getSensors()
                delay(100)
                checkSensorValues(sensors[0].value, sensors[1].value)
                _eventUiState.value = _eventUiState.value.copy(
                    temperature = sensors[0].value,
                    airQuality = sensors[1].value,
                )
            } catch (e: IOException) {
                e.printStackTrace()
                showNoConnectionNotification()
            } catch (e: SocketTimeoutException) {
                e.printStackTrace()
                showNoConnectionNotification()
            }
        }
    }

    private fun checkSensorValues(temperature: Float, airQuality: Float) {
        _eventUiState.value = _eventUiState.value.copy(
            dangerousTemperature = false
        )
        _eventUiState.value = _eventUiState.value.copy(
            dangerousAirQuality = false
        )
        if (temperature > 30){
            sendNotification("Temperature is too high, please go outside")
            _eventUiState.value = _eventUiState.value.copy(
                dangerousTemperature = true
            )
        }
        if (airQuality > 1000){ // https://www.co2meter.com/blogs/news/carbon-dioxide-indoor-levels-chart
            sendNotification("Airquality is too low, please go outside")
            _eventUiState.value = _eventUiState.value.copy(
                dangerousAirQuality = true
            )
        }
    }

        //Websockets
        fun setSensorValue(type: String, value: Float) {

        if (type == "temperature") {
            _eventUiState.value = _eventUiState.value.copy(temperature = value)
            checkSensorValues(value, _eventUiState.value.airQuality)
        } else {
            _eventUiState.value = _eventUiState.value.copy(airQuality = value)
            checkSensorValues(_eventUiState.value.temperature, value)
        }
    }

    // Emergencies
    @SuppressLint("MissingPermission")
    fun handleEmergency(
        usePreciseLocation: Boolean,
        scope: CoroutineScope,
        locationClient: FusedLocationProviderClient,
        type: String
    ) {
        if (type == "Cancel") {
            _eventUiState.update {
                it.copy(emergencyState = EmergencyState.DEFAULT)
            }
            return
        }
        scope.launch(Dispatchers.IO) {
            try {
                sendEmergencyApiCall(getLocation(usePreciseLocation, locationClient), type)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    @SuppressLint("MissingPermission")
    private suspend fun getLocation(usePreciseLocation: Boolean, locationClient: FusedLocationProviderClient): String {
        val priority = if (usePreciseLocation) {
            Priority.PRIORITY_HIGH_ACCURACY
        } else {
            Priority.PRIORITY_BALANCED_POWER_ACCURACY
        }
        val result = locationClient.getCurrentLocation(
            priority,
            CancellationTokenSource().token,
        ).await()
        return "${result.latitude},${result.longitude}"
    }

    private suspend fun sendEmergencyApiCall(location: String, type: String) {
        val emergency = Emergency(location, type)
        try {
            _eventUiState.update {
                it.copy(emergencyState = EmergencyState.EMERGENCY_SENT)
            }
            EventizApi.retrofitService.postEmergency(emergency)
        } catch (e: IOException) {
            e.printStackTrace()
            showNoConnectionNotification()
        } catch (e: SocketTimeoutException) {
            e.printStackTrace()
            showNoConnectionNotification()
        }
    }

    fun changeEmergencyState() {
        when (_eventUiState.value.emergencyState) {
            EmergencyState.DEFAULT -> {
                _eventUiState.update {
                    it.copy(emergencyState = EmergencyState.SHOW_EMERGENCY_OPTIONS)
                }
            }
            EmergencyState.SHOW_EMERGENCY_OPTIONS -> {
                // do nothing
            }
            else -> {
                _eventUiState.update {
                    it.copy(emergencyState = EmergencyState.DEFAULT)
                }
            }
        }
    }


    // Racks
    fun setRackNumber(number: String) {
        _eventUiState.update {
            it.copy(rackText = number)
        }
    }

    fun resetRackNumber() {
        _eventUiState.update {
            it.copy(rackText = defaultRackText)
        }
    }



    // Helpers
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun showNoConnectionNotification() {
        val context = getApplication<Application>().applicationContext
        Toast.makeText(context, "No connection to the server, please contact the staff", Toast.LENGTH_LONG).show()
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkNetworkConnectivity(): Boolean {
        val context = getApplication<Application>().applicationContext
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork
        if (networkCapabilities == null) {
            Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show()
            return false
        }
        return true
    }


    @SuppressLint("MissingPermission")
    fun sendNotification(s: String) {
        val context = getApplication<Application>().applicationContext;
        val notificationManager = NotificationManagerCompat.from(context)
        val notification = NotificationCompat.Builder(context, "sensors_channel")
            .setContentTitle("WARNING")
            .setContentText(s)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000, 1000))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        notificationManager.notify(2, notification)
    }
}