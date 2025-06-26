package com.example.geopet.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavController
import com.example.geopet.NotificationHelper
import com.google.android.gms.maps.model.LatLng


data class MascotaCercana(
    val nombre: String,
    val rareza: String,
    val distancia: String,
    val imagenUrl: String,
    val location: LatLng
)


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Buscar(navController: NavController,onBuscarMascota: (LatLng) -> Unit) {
    Scaffold(
        containerColor = VerdeClaro
    ) {
        Contenido_Pantalla_Buscar(navController, onBuscarMascota)
    }
}

@Composable
fun Contenido_Pantalla_Buscar(navController: NavController,onBuscarMascota: (LatLng) -> Unit) {
    val context = LocalContext.current

    val mascotas = listOf(
        MascotaCercana("Palomo", "Legendario", "300 m", "https://www.shutterstock.com/image-vector/anime-cartoon-character-orange-color-600nw-2407945115.jpg", LatLng(-15.823028, -70.018070)),
        MascotaCercana("Gato Mewing", "Ultra raro", "500 m", "https://www.shutterstock.com/image-vector/anime-cartoon-character-orange-color-600nw-2407945115.jpg", LatLng(-15.823106, -70.017402)),
        MascotaCercana("Perro Facha", "Raro", "1.2 km", "https://www.shutterstock.com/image-vector/anime-cartoon-character-orange-color-600nw-2407945115.jpg", LatLng(-15.822260, -70.017195)),
        MascotaCercana("Caracolito", "Legendario", "750 m", "https://www.shutterstock.com/image-vector/anime-cartoon-character-orange-color-600nw-2407945115.jpg", LatLng(-15.8225, -70.0185)),
        MascotaCercana("Paloma Táctica", "Legendario", "2.1 km", "https://www.shutterstock.com/image-vector/anime-cartoon-character-orange-color-600nw-2407945115.jpg", LatLng(-15.824, -70.0175))
    )


    var mascotaSeleccionada by remember { mutableStateOf<MascotaCercana?>(null) }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // 🆕 Encabezado superior
            Text(
                text = "Buscar mascotas cercanas",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = VerdeOscuro,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp),
                textAlign = TextAlign.Center
            )

            mascotas.forEach { mascota ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                        .background(Crema, shape = RoundedCornerShape(12.dp))
                        .clickable { mascotaSeleccionada = mascota }
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        AsyncImage(
                            model = mascota.imagenUrl,
                            contentDescription = mascota.nombre,
                            modifier = Modifier
                                .size(80.dp)
                                .clip(RoundedCornerShape(10.dp))
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = mascota.nombre,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = VerdeOscuro
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Distancia: ${mascota.distancia}",
                                fontSize = 14.sp,
                                color = VerdeOscuro
                            )
                        }
                    }
                }
            }

        }

        // Modal igual que antes...
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
                    colors = CardDefaults.cardColors(containerColor = VerdeClaro)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp)
                            .clickable(enabled = false) {},
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = mascota.imagenUrl,
                            contentDescription = mascota.nombre,
                            modifier = Modifier
                                .size(180.dp)
                                .clip(RoundedCornerShape(16.dp))
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
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Distancia: ${mascota.distancia}",
                            fontSize = 16.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                mascotaSeleccionada?.let {
                                    onBuscarMascota(it.location)
                                }
                                mascotaSeleccionada = null
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CremaClaro)
                        ) {
                            Text("Buscar")
                        }
                    }
                }
            }
    }
}
}
