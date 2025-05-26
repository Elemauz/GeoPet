package com.example.geopet

import androidx.compose.runtime.Composable
import com.google.android.gms.maps.model.LatLng

data class PetMarkerData(
    val imageResId: Int,
    val fullName: String,
    val location: LatLng
)
@Composable
fun RenderPetMarkers(
    markers: List<PetMarkerData>,
    onClick: (PetMarkerData) -> Unit
) {
    for (marker in markers) {
        CustomMapMarker(
            imageResId = marker.imageResId,
            fullName = marker.fullName,
            location = marker.location,
            onClick = { onClick(marker) }
        )
    }
}

//extraccion de data de la API MASCOTAS
//Simulacion de datos obtenidos
val petMarkers = listOf(
    PetMarkerData(R.drawable.mistery_pet_icon, "Perro Chamba 1", LatLng(-15.8404, -70.0353)),
    PetMarkerData(R.drawable.mistery_pet_icon, "Perro Chamba 2", LatLng(-15.8420, -70.0340)),
    PetMarkerData(R.drawable.mistery_pet_icon, "Perro Chamba 3", LatLng(-15.8450, -70.0325))
)