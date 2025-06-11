package com.example.geopet.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.geopet.data.api.RetrofitInstance
import com.example.geopet.data.model.ApiConstants.BASE_URL
import com.example.geopet.data.model.Pet

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Mascotas(navController: NavController) {
    Scaffold {
        Contenido_Pantalla_Mascotas(navController)
    }
}

@Composable
fun Contenido_Pantalla_Mascotas(navController: NavController) {
    var pets by remember { mutableStateOf<List<Pet>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        try {
            pets = RetrofitInstance.api.getPets()
        } catch (e: Exception) {
            Log.e("Pantalla_Mascotas", "Error al obtener mascotas: ${e.localizedMessage}")
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(pets) { pet ->
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                ) {
                    Image(
                        painter = rememberImagePainter(BASE_URL + pet.imagen_url.trimStart('/')),
                        contentDescription = pet.nombre,
                        modifier = Modifier
                            .size(64.dp)
                            .padding(end = 16.dp),
                        contentScale = ContentScale.Crop
                    )

                    Column {
                        Text(text = pet.nombre, style = MaterialTheme.typography.titleMedium)
                        Text(text = "Rareza: ${pet.rareza}", style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        }
    }
}
