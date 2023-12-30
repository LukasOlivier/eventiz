package com.example.eventiz

import android.Manifest
import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.eventiz.nfc.NfcService
import com.example.eventiz.ui.Dashboard
import com.example.eventiz.ui.foreground.ForegroundNotificationService
import com.example.eventiz.ui.theme.EventizTheme


@RequiresApi(Build.VERSION_CODES.Q)
class MainActivity : ComponentActivity() {
    private var foregroundNotificationService: ForegroundNotificationService? = null
    private val foregroundNotificationServiceIntent by lazy {
        Intent(this, ForegroundNotificationService::class.java)
    }

    private var nfcService = NfcService()

    @SuppressLint("MissingPermission") // checkAppPermissions() checks for permissions
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            EventizTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (checkAppPermissions() != true) {
                        return@Surface
                    }
                    nfcService.nfcAdapter = NfcAdapter.getDefaultAdapter(this)
                    nfcService.pendingIntent = PendingIntent.getActivity(
                        this,
                        0,
                        Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                        PendingIntent.FLAG_MUTABLE
                    )

                    foregroundNotificationService?.announcementServiceIntent =
                        Intent(this, ForegroundNotificationService::class.java)

                    showNotification()
                    Dashboard(nfcService.tagContent, ::updateNotification, ::clearNfcTag)
                }
            }
        }

    }

    private fun showNotification() {
        val intent = Intent(this, ForegroundNotificationService::class.java)
        intent.action = ForegroundNotificationService.Actions.START.toString()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent)
        } else {
            startService(intent)
        }
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    private fun checkAppPermissions(): Any {
        val permissions = arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.INTERNET,
            Manifest.permission.NFC,
            Manifest.permission.POST_NOTIFICATIONS,
        )
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.NFC
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED || ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.FOREGROUND_SERVICE

            ) != PackageManager.PERMISSION_GRANTED


        ) {
            ActivityCompat.requestPermissions(this, permissions, 1)
            return checkAppPermissions()
        }
        return true
    }

    private fun clearNfcTag() {
        nfcService.clearTag()
    }

    private fun bindService() {

        val serviceConnection = object : ServiceConnection {
            override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
                val binder = service as ForegroundNotificationService.AnnouncementServiceBinder
                foregroundNotificationService = binder.getService()
            }

            override fun onServiceDisconnected(name: ComponentName?) {
                foregroundNotificationService = null
            }
        }

        bindService(
            foregroundNotificationServiceIntent,
            serviceConnection,
            Context.BIND_AUTO_CREATE
        )
    }

    @SuppressLint("MissingPermission") // We check for permissions in the MainActivity
    fun updateNotification(title: String, contentText: String) {
        val notificationManager = NotificationManagerCompat.from(this)
        val notification = NotificationCompat.Builder(this, "announcements_channel")
            .setContentTitle(title)
            .setContentText(contentText)
            .setStyle(NotificationCompat.BigTextStyle().bigText(contentText))
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setOngoing(true)
            .build()

        notificationManager.notify(1, notification)
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(foregroundNotificationServiceIntent)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        nfcService.onNewIntent(intent)
    }

    override fun onResume() {
        super.onResume()
        nfcService.resume(this)
    }

    override fun onPause() {
        super.onPause()
        nfcService.pause(this)
    }

    override fun onStart() {
        super.onStart()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                "announcements_channel",
                "Announcements Channel",
                NotificationManager.IMPORTANCE_LOW
            )
            val sensorNotificationChannel = NotificationChannel(
                "sensors_channel",
                "Sensor warnings",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
            notificationManager.createNotificationChannel(sensorNotificationChannel)
        }
        bindService()
    }
}




