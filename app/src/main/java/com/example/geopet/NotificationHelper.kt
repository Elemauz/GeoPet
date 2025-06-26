package com.example.geopet

import android.Manifest
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat


object NotificationHelper {

    private fun createNotificationIntent(context: Context): PendingIntent {
        val intent = Intent(context, MainActivity2::class.java).apply {
            putExtra("destino", "Pantalla_Mascotas") // por ejemplo
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }

        return PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }
    fun mostrarNotificacion(context: Context, titulo: String, mensaje: String) {
        val pendingIntent = createNotificationIntent(context)
            val permissionGranted = context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            if (!permissionGranted) return // No mostrar si no hay permiso


        val builder = NotificationCompat.Builder(context, "geopet_channel")
            .setSmallIcon(R.mipmap.doberman_notification_mdpi) // cambia al ícono que tengas
            .setContentTitle(titulo)
            .setContentText(mensaje)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        with(NotificationManagerCompat.from(context)) {
            notify(1001, builder.build())
        }
    }
}
