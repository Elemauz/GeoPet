package com.example.geopet

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build

class GeoPetApp : Application() {

    override fun onCreate() {
        super.onCreate()
        CrearCanalDeNotificacion()
    }

    private fun CrearCanalDeNotificacion() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "geopet_channel",
                "GeoPet Notificaciones",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Canal para notificaciones de GeoPet"
            }
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
}
