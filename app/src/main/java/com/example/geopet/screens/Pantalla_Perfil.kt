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
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.geopet.R
import com.example.geopet.data.model.ApiConstants.BASE_URL
import com.example.geopet.firebase.auth.FirebaseAuthManager
import com.example.geopet.firebase.data.MascotaCollectionManager
import com.example.geopet.firebase.model.MascotaFirebase
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Perfil(navController: NavController, onLogout: () -> Unit) {
    val user = FirebaseAuthManager.getCurrentUser()
    Scaffold {
        Contenido_Pantalla_Perfil(user, navController, onLogout)
    }
}

@Composable
fun Contenido_Pantalla_Perfil(user: FirebaseUser?, navController: NavController, onLogout: () -> Unit) {
    val nombre = user?.displayName ?: "Nombre no disponible"
    val correo = user?.email ?: "Correo no disponible"
    val fotoUrl = user?.photoUrl?.toString()

    val mascotasCount = remember { mutableStateOf(0) }
    val ultimaMascota = remember { mutableStateOf<MascotaFirebase?>(null) }

    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        try {
            val mascotas = MascotaCollectionManager.obtenerTodasLasMascotas()
            mascotasCount.value = mascotas.size
            ultimaMascota.value = mascotas.lastOrNull()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

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

            if (fotoUrl != null) {
                AsyncImage(
                    model = fotoUrl,
                    contentDescription = "Foto de perfil",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.default_profile),
                    contentDescription = "Foto por defecto",
                    modifier = Modifier
                        .size(130.dp)
                        .clip(CircleShape)
                        .background(Color.Gray),
                    contentScale = ContentScale.Crop
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

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
                        text = nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = VerdeOscuro
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Correo",
                            tint = VerdeClaro,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = correo,
                            fontSize = 16.sp,
                            color = VerdeClaro
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Divider(color = VerdeClaro, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = "Mascotas",
                            tint = VerdeOscuro,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Mascotas encontradas: ${mascotasCount.value}",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            color = VerdeOscuro
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))
                    Text(
                        text = "Mascota favorita",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = VerdeOscuro
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    ultimaMascota.value?.let { mascota ->
                        val imagenFinalUrl = if (mascota.imagen_url.startsWith("http")) {
                            mascota.imagen_url
                        } else {
                            BASE_URL.trimEnd('/') + mascota.imagen_url
                        }
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            AsyncImage(
                                model = imagenFinalUrl,
                                contentDescription = mascota.nombre,
                                modifier = Modifier
                                    .size(150.dp)
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
                            Text(
                                text = mascota.rareza,
                                fontSize = 14.sp,
                                color = VerdeClaro,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                    } ?: Text(
                        text = "Aún no has recolectado mascotas.",
                        fontSize = 14.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        IconButton(
            onClick = {
                FirebaseAuthManager.signOut()
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
