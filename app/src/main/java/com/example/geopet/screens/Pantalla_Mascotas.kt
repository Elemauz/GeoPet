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
import com.example.geopet.data.api.RetrofitInstance
import com.example.geopet.data.model.Pet
import com.example.geopet.data.model.ApiConstants
import com.example.geopet.data.model.ApiConstants.BASE_URL
import kotlinx.coroutines.launch

val VerdeOscuro = Color(0xFF4A5759)
val VerdeClaro = Color(0xFFB0C4B1)
val Crema = Color(0xFFDEDBD2)
val CremaClaro = Color(0xFFF7E1D7)

//val imageUrl = BASE_URL.trimEnd('/') + "/" + mascota.imagen_url.trimStart('/')

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Mascotas(navController: NavController) {
    Scaffold (
        containerColor = VerdeClaro
    ){
        Contenido_Pantalla_Mascotas(navController)
    }
}

@Composable
fun Contenido_Pantalla_Mascotas(navController: NavController) {
    var mascotas by remember { mutableStateOf<List<Pet>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            mascotas = RetrofitInstance.api.getAllPets()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    MascotaGridConDetalle(
        mascotas = mascotas,
        baseUrl = BASE_URL
    )
}

@Composable
fun MascotaGridConDetalle(mascotas: List<Pet>, baseUrl: String) {
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
                        val imageUrl = baseUrl.trimEnd('/') + mascota.imagen_url

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
                                    model = imageUrl,
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
                        Spacer(modifier = Modifier.width(150.dp))
                    }
                }
            }
        }

        mascotaSeleccionada?.let { mascota ->
            val imageUrl = baseUrl.trimEnd('/') + mascota.imagen_url

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
                            .padding(20.dp)
                            .clickable(enabled = false) {},
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        AsyncImage(
                            model = imageUrl,
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
