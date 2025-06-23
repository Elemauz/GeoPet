package com.example.geopet
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.compose.runtime.*
import com.google.android.gms.location.*

@SuppressLint("MissingPermission")
@Composable
fun LocationUpdates(context: Context): State<Location?> {
    val fusedLocationClient = remember {
        LocationServices.getFusedLocationProviderClient(context)
    }

    val locationState = remember { mutableStateOf<Location?>(null) }

    val locationCallback = remember {
        object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                super.onLocationResult(result)
                locationState.value = result.lastLocation
            }
        }
    }

    LaunchedEffect(Unit) {
        val locationRequest = LocationRequest.Builder(
            Priority.PRIORITY_HIGH_ACCURACY,
            2000L         /// Frecuencia de "POST" ubicacion
        ).build()

        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            Looper.getMainLooper()
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            fusedLocationClient.removeLocationUpdates(locationCallback)
        }
    }
    return locationState
}