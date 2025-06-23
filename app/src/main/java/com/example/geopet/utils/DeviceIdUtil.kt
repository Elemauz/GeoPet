package com.example.geopet.utils

import android.content.Context
import java.util.UUID

object DeviceIdUtil {
    private const val PREFS_NAME = "device_prefs"
    private const val DEVICE_ID_KEY = "device_id"

    fun getDeviceId(context: Context): String {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        var id = prefs.getString(DEVICE_ID_KEY, null)

        if (id == null) {
            id = UUID.randomUUID().toString()
            prefs.edit().putString(DEVICE_ID_KEY, id).apply()
        }

        return id
    }
}
