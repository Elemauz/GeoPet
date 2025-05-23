package com.example.geopet.screens
import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.google.maps.android.compose.GoogleMap

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
    Box(
        Modifier
            .fillMaxSize()
            .background(Color.Yellow)
    ){
        GoogleMap()
    }


}

