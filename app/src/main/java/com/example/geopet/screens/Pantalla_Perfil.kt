package com.example.geopet.screens

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
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
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.example.geopet.NotificationHelper


// MODELOS
data class PerfilUsuario(
    val fotoUrl: String,
    val nombre: String,
    val correo: String,
    val mascotasEncontradas: Int
)



@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Perfil(navController: NavController,
                    onLogout: () -> Unit) {
    Scaffold {
        Contenido_Pantalla_Perfil(navController,
            onLogout)
    }
}

@Composable
fun Contenido_Pantalla_Perfil(navController: NavController,
                              onLogout: () -> Unit) {
    val context = LocalContext.current

    val usuarioPrueba = PerfilUsuario(
        fotoUrl = "https://randomuser.me/api/portraits/women/65.jpg",
        nombre = "Valeria Gómez",
        correo = "valeria.gomez@email.com",
        mascotasEncontradas = 12
    )

    val mascotaFavorita = Mascota(
        nombre = "Palomo",
        rareza = "legendario",
        imagen_url = "https://www.shutterstock.com/image-vector/anime-cartoon-character-orange-color-600nw-2407945115.jpg"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(CremaClaro)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(30.dp))

            // Foto de perfil
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(context)
                        .data(usuarioPrueba.fotoUrl)
                        .crossfade(true)
                        .build()
                ),
                contentDescription = "Foto de perfil",
                modifier = Modifier
                    .size(130.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Card de perfil
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Crema),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = usuarioPrueba.nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Correo",
                            tint = VerdeClaro,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = usuarioPrueba.correo,
                            fontSize = 16.sp,
                            color = VerdeClaro
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = VerdeClaro, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Mascotas",
                            tint = VerdeOscuro,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mascotas encontradas: ${usuarioPrueba.mascotasEncontradas}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = VerdeOscuro
                        )
                    }

                    // MASCOTA FAVORITA
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Mascota favorita",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VerdeOscuro
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AsyncImage(
                            model = mascotaFavorita.imagen_url,
                            contentDescription = mascotaFavorita.nombre,
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(16.dp)),
                            contentScale = ContentScale.Crop
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = mascotaFavorita.nombre,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = VerdeOscuro,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Text(
                            text = mascotaFavorita.rareza,
                            fontSize = 14.sp,
                            color = VerdeClaro,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            /*Button(
                onClick = {
                    NotificationHelper.mostrarNotificacion(
                        context,
                        titulo = "¡GeoPet te llama!",
                        mensaje = "Ve a revisar tus mascotas 🐾"
                    )
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeClaro,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Lanzar notificación")
            }*/
            }

            // BOTÓN DE CERRAR SESIÓN EN ESQUINA SUPERIOR DERECHA
            IconButton(
            onClick = {
                onLogout()
            },
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(16.dp)
            ) {
            Icon(
                imageVector = Icons.Default.ExitToApp,
                contentDescription = "Cerrar sesión",
                tint = VerdeOscuro
            )
            }
            }
            }
