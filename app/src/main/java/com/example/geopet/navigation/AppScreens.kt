package com.example.geopet.navigation

import com.example.geopet.R

sealed class AppScreens(
    val route: String,
    val label: String,
    val iconResId: Int
) {
    object Pantalla_Inicio : AppScreens("Pantalla_Inicio", "Inicio", R.drawable.home)
    object Pantalla_Mascotas : AppScreens("Pantalla_Mascotas", "Mascotas", R.drawable.pet)
    object Pantalla_Buscar : AppScreens("Pantalla_Buscar", "Buscar", R.drawable.route_icon)
    object Pantalla_Perfil : AppScreens("Pantalla_Perfil", "Perfil", R.drawable.cuenta_picture)
    object Pantalla_Login : AppScreens("Pantalla_Login", "Login", R.drawable.cuenta_picture)
    object Pantalla_Registro: AppScreens("Pantalla_Registro", "Registro", R.drawable.cuenta_picture)


}
