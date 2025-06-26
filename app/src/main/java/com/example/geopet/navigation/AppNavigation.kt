package com.example.geopet.navigation
import android.Manifest
import android.app.Activity
import androidx.compose.runtime.mutableStateOf
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.geopet.screens.Pantalla_Buscar
import com.example.geopet.screens.Pantalla_Inicio
import com.example.geopet.screens.Pantalla_Mascotas
import com.example.geopet.screens.Pantalla_Perfil
import com.example.geopet.screens.Pantalla_Login
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.geopet.screens.Pantalla_Registro
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalPermissionsApi::class, ExperimentalPermissionsApi::class)
@Composable
fun AppNavigation(destino: String? = null) {
    val navController = rememberNavController()
    val context = LocalContext.current
    val activity = context as? Activity
    val isLoggedIn = remember { mutableStateOf(false) }
    val intentDestino = activity?.intent?.getStringExtra("destino")
    val destinoNotificacion = remember { mutableStateOf<String?>(null) }

    val permissionState = rememberMultiplePermissionsState(
        permissions = listOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.POST_NOTIFICATIONS
        )
    )
    LaunchedEffect(Unit) {
        if (!permissionState.allPermissionsGranted) {
            permissionState.launchMultiplePermissionRequest()
        }
        destinoNotificacion.value = intentDestino
    }

    LaunchedEffect(destinoNotificacion.value) {
        destinoNotificacion.value?.let {
            navController.navigate(it) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
            destinoNotificacion.value = null
        }
    }


    LaunchedEffect(destino) {
        destino?.let {
            navController.navigate(it)
        }
    }

    Scaffold(
        bottomBar = {
            val currentDestination = navController.currentBackStackEntryAsState().value?.destination?.route
            if (currentDestination in listOf(
                    AppScreens.Pantalla_Inicio.route,
                    AppScreens.Pantalla_Mascotas.route,
                    AppScreens.Pantalla_Buscar.route,
                    AppScreens.Pantalla_Perfil.route
                )
            ) {
                BottomNavigationBar(navController = navController)
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AppScreens.Pantalla_Login.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(AppScreens.Pantalla_Login.route) {
                Pantalla_Login(
                    onLoginSuccess = {
                        navController.navigate(AppScreens.Pantalla_Inicio.route) {
                            popUpTo(AppScreens.Pantalla_Login.route) { inclusive = true }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate(AppScreens.Pantalla_Registro.route)
                    }
                )
            }

            composable(AppScreens.Pantalla_Registro.route) {
                Pantalla_Registro(
                    onRegisterSuccess = {
                        navController.navigate(AppScreens.Pantalla_Inicio.route) {
                            popUpTo(AppScreens.Pantalla_Login.route) { inclusive = true }
                        }
                    },
                    onLoginClick = {
                        navController.navigate(AppScreens.Pantalla_Login.route) {
                            popUpTo(AppScreens.Pantalla_Registro.route) { inclusive = true }
                        }
                    }
                )
            }

            composable(AppScreens.Pantalla_Inicio.route) {
                Pantalla_Inicio(navController)
            }

            composable(AppScreens.Pantalla_Mascotas.route) {
                Pantalla_Mascotas(navController)
            }

            composable(AppScreens.Pantalla_Buscar.route) {
                Pantalla_Buscar(navController)
            }

            composable(AppScreens.Pantalla_Perfil.route) {
                Pantalla_Perfil(
                    navController = navController,
                    onLogout = {
                        navController.navigate(AppScreens.Pantalla_Login.route) {
                            popUpTo(0) { inclusive = true } // 🔥 Limpia el backstack
                        }
                    }
                )
            }
        }
    }

}




