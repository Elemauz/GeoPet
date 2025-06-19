package com.example.geopet.screens
import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.geopet.NotificationHelper

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Perfil(navController: NavController){
    Scaffold {
        Contenido_Pantalla_Perfil(navController)
    }
}

@Composable
fun Contenido_Pantalla_Perfil(navController: NavController) {
    val context = LocalContext.current

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Hola wawitas prueba de navegacion Pantalla Buscar")

        Button(onClick = {
            NotificationHelper.mostrarNotificacion(
                context,
                titulo = "¡GeoPet te llama!",
                mensaje = "Ve a revisar tus mascotas 🐾"
            )
        }) {
            Text("Lanzar notificación")
        }
    }
}