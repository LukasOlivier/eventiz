package com.example.eventiz.ui.foreground

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.eventiz.R

class ForegroundNotificationService : Service() {
    var announcementServiceIntent: Intent? = null
    private val notificationId = 1
    private val notificationManager by lazy {
        getSystemService(NOTIFICATION_SERVICE) as NotificationManager
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            Actions.START.toString() -> startForegroundService()
            Actions.STOP.toString() -> stopSelf()
        }
        return super.onStartCommand(intent, flags, startId)
    }


    inner class AnnouncementServiceBinder : Binder() {
        fun getService(): ForegroundNotificationService {
            return this@ForegroundNotificationService
        }
    }

    private fun startForegroundService() {
        val notification = NotificationCompat.Builder(this, "announcements_channel")
            .setContentTitle("Eventiz")
            .setContentText("No connection to server")
            .setOngoing(true)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .build()

        startForeground(notificationId, notification)
    }


    enum class Actions {
        START,
        STOP
    }
}
