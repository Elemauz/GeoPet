package com.example.geopet.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage

val VerdeOscuro = Color(0xFF4A5759)
val VerdeClaro = Color(0xFFB0C4B1)
val Crema = Color(0xFFDEDBD2)
val CremaClaro = Color(0xFFF7E1D7)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Mascotas(navController: NavController) {
    Scaffold (
        containerColor = VerdeClaro
    ){
        Contenido_Pantalla_Mascotas(navController)
    }
}

data class Mascota(
    val nombre: String,
    val rareza: String,
    val imagen_url: String
)

@Composable
fun Contenido_Pantalla_Mascotas(navController: NavController) {
    val mascotas = listOf(
        Mascota("Palomo", "legendario", ""),
        Mascota("Gato Mewing", "ultra raro", ""),
        Mascota("Palomo", "legendario", ""),
        Mascota("Gato Facha", "raro", ""),
        Mascota("Caracolito", "legendario", ""),
        Mascota("Palomo", "legendario", ""),
        Mascota("gato f", "legendario", ""),
        Mascota("Paloma tactica", "legendario", ""),
        Mascota("gato tactico", "legendario", ""),
        Mascota("Perro chamba", "legendario", ""),
        Mascota("Perro facha", "legendario", ""),
        Mascota("Palomo", "legendario", "")

    )

    MascotaGridConDetalle(
        mascotas = mascotas,
        baseUrl = "https://www.shutterstock.com/image-vector/anime-cartoon-character-orange-color-600nw-2407945115.jpg"
    )
}

@Composable
fun MascotaGridConDetalle(mascotas: List<Mascota>, baseUrl: String) {
    var mascotaSeleccionada by remember { mutableStateOf<Mascota?>(null) }

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
                            contentAlignment = Alignment.Center // Centra el contenido dentro del Box
                        ) {
                            Column(
                                horizontalAlignment = Alignment.CenterHorizontally,
                                verticalArrangement = Arrangement.Center,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                AsyncImage(
                                    model = baseUrl,
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
                            }
                        }

                    }
                    if (row.size < 2) {
                        Spacer(modifier = Modifier.width(150.dp)) // Para centrar visualmente la fila impar
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
                            .fillMaxWidth() // Asegura que todo esté centrado horizontalmente
                            .padding(20.dp)
                            .clickable(enabled = false) {}, // Desactiva el clic en la tarjeta
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = baseUrl,
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
