package com.example.geopet.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.core.graphics.drawable.toBitmap
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL

suspend fun getBitmapDescriptorFromUrl(context: Context, imageUrl: String): BitmapDescriptor? {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL(imageUrl)
            val connection = url.openConnection() as HttpURLConnection
            connection.doInput = true
            connection.connect()
            val input: InputStream = connection.inputStream
            val bitmap: Bitmap = BitmapFactory.decodeStream(input)
            BitmapDescriptorFactory.fromBitmap(Bitmap.createScaledBitmap(bitmap, 100, 100, false))
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}
