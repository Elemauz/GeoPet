package com.example.geopet.screens
import android.annotation.SuppressLint
import android.util.Log
import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Inicio(navController: NavController){
    Scaffold {
        Contenido_Pantalla_Inicio(navController)
    }
}

@Composable
fun Contenido_Pantalla_Inicio(navController: NavController){
    val context = LocalContext.current
    val locationState by LocationUpdates(context)
    val cameraPositionState = rememberCameraPositionState()
    val mapModifiers by remember { mutableStateOf(Modifier .fillMaxSize())}
    val mapProperties by remember { mutableStateOf(MapProperties(mapType = MapType.NORMAL))}
    val mapUIsettings by remember { mutableStateOf(MapUiSettings(zoomControlsEnabled = false))}
    val scope = rememberCoroutineScope()


    // Centrar la cámara cuando se actualice la ubicación
    LaunchedEffect(locationState) {
        locationState?.let {
            centerMapOnLocation(it, cameraPositionState, scope)
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
        }
    }

}
