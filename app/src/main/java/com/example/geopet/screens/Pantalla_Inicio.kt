package com.example.geopet.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.geopet.LocationUpdates
import com.example.geopet.centerMapOnLocation
import com.example.geopet.data.api.RetrofitInstance
import com.example.geopet.data.model.Pet
import com.example.geopet.utils.getBitmapDescriptorFromUrl
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.*
import com.example.geopet.data.model.ApiConstants
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Inicio(navController: NavController) {
    Scaffold {
        Contenido_Pantalla_Inicio(navController)
    }
}

@Composable
fun Contenido_Pantalla_Inicio(navController: NavController) {
    val context = LocalContext.current
    val locationState by LocationUpdates(context)
    val cameraPositionState = rememberCameraPositionState()
    val scope = rememberCoroutineScope()
    var petList by remember { mutableStateOf<List<Pet>>(emptyList()) }
    var markerIcons by remember { mutableStateOf<Map<Int, BitmapDescriptor>>(emptyMap()) }

    // Cargar mascotas
    LaunchedEffect(Unit) {
        try {
            val pets = RetrofitInstance.api.getAllPets()
            petList = pets
            val icons = mutableMapOf<Int, BitmapDescriptor>()
            pets.forEach { pet ->
                val url = ApiConstants.BASE_URL.trimEnd('/') + pet.imagen_url
                val descriptor = getBitmapDescriptorFromUrl(context, url)
                icons[pet.id] = descriptor ?: BitmapDescriptorFactory.defaultMarker()
            }
            markerIcons = icons
        } catch (e: Exception) {
            Log.e("Pantalla_Inicio", "Error al obtener mascotas: ${e.localizedMessage}")
        }
    }

    // Centrar mapa y enviar ubicación al backend
    LaunchedEffect(locationState) {
        locationState?.let { location ->
            // Centrar mapa
            //centerMapOnLocation(location, cameraPositionState, scope)

            // Enviar ubicación al backend
            try {
                RetrofitInstance.api.enviarUbicacion(
                    com.example.geopet.data.api.UbicacionRequest(
                        lat = location.latitude,
                        lon = location.longitude
                    )
                )
                Log.d("Pantalla_Inicio", "Ubicación enviada: ${location.latitude}, ${location.longitude}")
            } catch (e: Exception) {
                Log.e("Pantalla_Inicio", "Error al enviar ubicación: ${e.localizedMessage}")
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(mapType = MapType.NORMAL),
            uiSettings = MapUiSettings(zoomControlsEnabled = false)
        ) {
            locationState?.let { location ->
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "Ubicación actual"
                )
            }

            // Renderizar marcadores de mascotas con íconos
            for (pet in petList) {
                val position = LatLng(pet.lat, pet.lon)
                val icon = markerIcons[pet.id] ?: BitmapDescriptorFactory.defaultMarker()
                Marker(
                    state = MarkerState(position = position),
                    title = pet.nombre,
                    icon = icon
                )
            }
        }
    }
}
