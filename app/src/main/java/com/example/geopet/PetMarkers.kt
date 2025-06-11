package com.example.geopet

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.example.geopet.data.model.ApiConstants
import com.example.geopet.data.model.Pet
import com.example.geopet.utils.getBitmapDescriptorFromUrl
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState

data class PetMarkerData(
    val imageResId: Int,
    val fullName: String,
    val location: LatLng
)

@Composable
fun RenderPetMarkersWithImages(
    context: Context,
    pets: List<Pet>
) {
    val scope = rememberCoroutineScope()
    val markerStates = remember { mutableStateMapOf<Int, BitmapDescriptor?>() }

    pets.forEach { pet ->
        val markerPosition = LatLng(pet.lat, pet.lon)

        val descriptor = markerStates[pet.hashCode()]
        if (descriptor != null) {
            Marker(
                state = MarkerState(position = markerPosition),
                title = pet.nombre,
                icon = descriptor
            )
        } else {
            LaunchedEffect(pet.hashCode()) {
                val fullUrl = ApiConstants.BASE_URL + pet.imagen_url.trimStart('/')
                val icon = getBitmapDescriptorFromUrl(context, fullUrl)
                markerStates[pet.hashCode()] = icon
            }
        }
    }
}
