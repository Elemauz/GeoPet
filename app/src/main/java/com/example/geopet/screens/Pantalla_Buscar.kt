package com.example.geopet.screens

import android.annotation.SuppressLint
import android.location.Location
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.geopet.LocationUpdates
import com.example.geopet.data.api.RetrofitInstance
import com.example.geopet.data.api.UbicacionRequest
import com.example.geopet.data.model.ApiConstants
import com.example.geopet.data.model.Pet
import com.example.geopet.utils.DeviceIdUtil
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun buildImageUrl(base: String, path: String): String {
    return base.trimEnd('/') + path
}

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Buscar(navController: NavController) {
    Scaffold(
        containerColor = VerdeClaro
    ) {
        Contenido_Pantalla_Buscar(navController)
    }
}

@Composable
fun Contenido_Pantalla_Buscar(navController: NavController) {
    val context = LocalContext.current
    val locationState by LocationUpdates(context)
    val deviceId = remember { DeviceIdUtil.getDeviceId(context) }
    var mascotas by remember { mutableStateOf<List<Pet>>(emptyList()) }
    var userLocation by remember { mutableStateOf<Location?>(null) }
    var orden by remember { mutableStateOf("asc") }

    // Enviar ubicación cada 45 segundos
    LaunchedEffect(locationState) {
        while (true) {
            locationState?.let { location ->
                userLocation = location
                try {
                    RetrofitInstance.api.enviarUbicacion(
                        UbicacionRequest(
                            id_telefono = deviceId,
                            lat = location.latitude,
                            lon = location.longitude
                        )
                    )
                    Log.d("Pantalla_Buscar", "Ubicación enviada: ${location.latitude}, ${location.longitude}")
                } catch (e: Exception) {
                    Log.e("Pantalla_Buscar", "Error al enviar ubicación: ${e.localizedMessage}")
                }
            }
            delay(45000L) // 45 segundos
        }
    }

    // Obtener mascotas ordenadas
    LaunchedEffect(userLocation, orden) {
        try {
            mascotas = RetrofitInstance.api.getPetsOrdenadas(
                idTelefono = deviceId,
                orden = orden
            )
        } catch (e: Exception) {
            Log.e("Pantalla_Buscar", "Error al obtener mascotas: ${e.localizedMessage}")
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Button(
                onClick = { orden = "asc" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (orden == "asc") VerdeOscuro else CremaClaro
                )
            ) {
                Text("Más cercanas")
            }
            Spacer(modifier = Modifier.width(16.dp))
            Button(
                onClick = { orden = "desc" },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (orden == "desc") VerdeOscuro else CremaClaro
                )
            ) {
                Text("Más lejanas")
            }
        }

        MascotaGridOrdenada(
            mascotas = mascotas,
            baseUrl = ApiConstants.BASE_URL
        )
    }
}

@Composable
fun MascotaGridOrdenada(mascotas: List<Pet>, baseUrl: String) {
    var mascotaSeleccionada by remember { mutableStateOf<Pet?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Crema)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            mascotas.chunked(2).forEach { row ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 8.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    row.forEach { mascota ->
                        Box(
                            modifier = Modifier
                                .width(150.dp)
                                .padding(8.dp)
                                .background(Crema, shape = RoundedCornerShape(12.dp))
                                .clickable { mascotaSeleccionada = mascota },
                            contentAlignment = Alignment.Center
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AsyncImage(
                                    model = buildImageUrl(ApiConstants.BASE_URL, mascota.imagen_url),
                                    contentDescription = mascota.nombre,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = mascota.nombre,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    textAlign = TextAlign.Center,
                                    color = VerdeOscuro,
                                    modifier = Modifier.fillMaxWidth()
                                )
                                mascota.distancia_km?.let { distancia ->
                                    val texto = if (distancia >= 1.0) {
                                        "A ${"%.2f".format(distancia)} km"
                                    } else {
                                        "A ${"%.0f".format(distancia * 1000)} m"
                                    }
                                    Text(
                                        text = texto,
                                        fontSize = 12.sp,
                                        color = Color.DarkGray
                                    )
                                }
                            }
                        }
                    }
                    if (row.size < 2) {
                        Spacer(modifier = Modifier.width(150.dp))
                    }
                }
            }
        }

        mascotaSeleccionada?.let { mascota ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.6f))
                    .clickable { mascotaSeleccionada = null },
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth(0.85f)
                        .wrapContentHeight()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = CremaClaro)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = "${ApiConstants.BASE_URL}${mascota.imagen_url}",
                            contentDescription = mascota.nombre,
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = mascota.nombre,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Rareza: ${mascota.rareza}",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        mascota.distancia_km?.let { distancia ->
                            val texto = if (distancia >= 1.0) {
                                "Distancia: ${"%.2f".format(distancia)} km"
                            } else {
                                "Distancia: ${"%.0f".format(distancia * 1000)} m"
                            }
                            Text(
                                text = texto,
                                fontSize = 14.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = { mascotaSeleccionada = null },
                            colors = ButtonDefaults.buttonColors(containerColor = VerdeOscuro)
                        ) {
                            Text("Cerrar")
                        }
                    }
                }
            }
        }
    }
}
