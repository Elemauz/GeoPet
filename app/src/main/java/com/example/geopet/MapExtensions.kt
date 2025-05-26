package com.example.geopet


import android.location.Location
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.CameraPositionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

fun centerMapOnLocation(
    location: Location,
    cameraPositionState: CameraPositionState,
    scope: CoroutineScope,
    zoom: Float = 16f
) {
    val latLng = LatLng(location.latitude, location.longitude)
    val cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, zoom)

    scope.launch {
        cameraPositionState.animate(cameraUpdate)
    }
}
