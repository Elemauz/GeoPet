package com.example.geopet.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.geopet.LocationUpdates
import com.example.geopet.centerMapOnLocation
import com.example.geopet.data.api.RetrofitInstance
import com.example.geopet.data.api.UbicacionRequest
import com.example.geopet.data.model.Pet
import com.example.geopet.utils.getBitmapDescriptorFromUrl
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.maps.android.compose.*
import com.example.geopet.data.model.ApiConstants
import com.example.geopet.utils.DeviceIdUtil
import androidx.compose.material.icons.rounded.LocationOn
import androidx.compose.material.icons.rounded.LocationOff
import androidx.compose.ui.Alignment
import com.example.geopet.firebase.data.MascotaCollectionManager
import com.google.api.Context


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
    val deviceId = remember { DeviceIdUtil.getDeviceId(context) }

    var seguirUbicacion by remember { mutableStateOf(false) }


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
            if (seguirUbicacion) {
                centerMapOnLocation(location, cameraPositionState, scope)
            }

            try {
                // 1. Enviar ubicación al backend
                RetrofitInstance.api.enviarUbicacion(
                    UbicacionRequest(
                        id_telefono = deviceId,
                        lat = location.latitude,
                        lon = location.longitude
                    )
                )
                Log.d("Pantalla_Inicio", "Ubicación enviada con ID: $deviceId")

                // 2. Recolectar mascotas cercanas
                MascotaCollectionManager.recolectarMascotasCercanas(
                    context = context,
                    mascotas = petList,
                    ubicacionUsuarioLat = location.latitude,
                    ubicacionUsuarioLon = location.longitude
                )
            } catch (e: Exception) {
                Log.e("Pantalla_Inicio", "Error en ubicación/recolección: ${e.localizedMessage}")
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
        FloatingActionButton(
            onClick = {
                seguirUbicacion = !seguirUbicacion
                if (seguirUbicacion) {
                    locationState?.let { centerMapOnLocation(it, cameraPositionState, scope) }
                }
            },
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Icon(
                imageVector = if (seguirUbicacion) Icons.Rounded.LocationOn else Icons.Rounded.LocationOff,
                contentDescription = if (seguirUbicacion) "Seguimiento activado" else "Seguimiento desactivado"
            )
        }
    }
}