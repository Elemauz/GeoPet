package com.example.geopet.screens
import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.geopet.CustomMapMarker
import com.example.geopet.LocationUpdates
import com.example.geopet.R
import com.example.geopet.RenderPetMarkers
import com.example.geopet.centerMapOnLocation
import com.example.geopet.petMarkers
import com.example.geopet.polyline.obtenerRutaDesdeGoogle
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.rememberCameraPositionState
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Inicio(navController: NavController,
                    destinoSeleccionado: MutableState<LatLng?>){
    Scaffold {
        Contenido_Pantalla_Inicio(navController,
            destinoSeleccionado)
    }
}

@Composable
fun Contenido_Pantalla_Inicio(navController: NavController,
                              destinoSeleccionado: MutableState<LatLng?>
){
    val context = LocalContext.current
    val locationStateDelegate = LocationUpdates(context)
    val locationState by locationStateDelegate

    val cameraPositionState = rememberCameraPositionState()
    val mapModifiers by remember { mutableStateOf(Modifier .fillMaxSize())}
    val mapProperties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL))}
    val mapUIsettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = true))}
    val scope = rememberCoroutineScope()
    val ruta = remember { mutableStateOf<List<LatLng>>(emptyList()) }


    /*LaunchedEffect(locationState) {
        locationState?.let {
            centerMapOnLocation(it, cameraPositionState, scope)
        }
    }*/

    LaunchedEffect(destinoSeleccionado.value, locationState) {
        if (destinoSeleccionado.value != null && locationState != null) {
            val ubicacionActual = locationState
            if (ubicacionActual != null && destinoSeleccionado.value != null) {
                val origen = LatLng(ubicacionActual.latitude, ubicacionActual.longitude)
                val destino = destinoSeleccionado.value!!
                val puntosRuta = obtenerRutaDesdeGoogle(origen, destino, "AIzaSyB3S_g1pxZVAi8NWMdrR5PGwX8HQ2n2t0o")
                ruta.value = puntosRuta
            }
        }
    }

    Box(
        modifier = Modifier.fillMaxSize(),

    ) {
        GoogleMap(
            modifier = mapModifiers,
            properties = mapProperties,
            uiSettings = mapUIsettings,
            cameraPositionState = cameraPositionState,

        ) {


            locationState?.let { location ->
                RenderPetMarkers(petMarkers) { }
                Marker(
                    state = MarkerState(position = LatLng(location.latitude, location.longitude)),
                    title = "Ubicación actual"

                )
            }
            Polyline(
                points = ruta.value,
                color = Color.Red,
                width = 8f
            )

        }
        FloatingActionButton(
            onClick = {
                locationState?.let {
                    centerMapOnLocation(it, cameraPositionState, scope)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp, bottom = 16.dp),
            containerColor = VerdeClaro
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Centrar en mi ubicación",
                tint = Color.White
            )
        }
    }

}
