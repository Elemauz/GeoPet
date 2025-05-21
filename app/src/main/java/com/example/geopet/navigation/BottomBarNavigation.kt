package com.example.geopet.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import com.example.geopet.R


@Composable
fun BottomNavigationBar(navController: NavHostController) {
    val items = listOf(
        AppScreens.Pantalla_Inicio,
        AppScreens.Pantalla_Mascotas,
        AppScreens.Pantalla_Buscar,
        AppScreens.Pantalla_Perfil
    )

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val itemWidth = screenWidth / 5

    NavigationBar(
        containerColor = Color(0xFFB0C4B1), // fondo barra
        tonalElevation = 0.dp, // sin sombra para aspecto plano
        modifier = Modifier
            .fillMaxWidth()
            .height(135.dp)
            .navigationBarsPadding() // para que respete el área del sistema
            .background(Color(0xFF4A5759))// ajusta altura si quieres más espacio
    ) {
        items.forEach { screen ->
            val selected = currentRoute == screen.route

            NavigationBarItem(
                icon = {
                    Box(
                        modifier = Modifier
                            .offset(y = (4).dp)
                            .fillMaxSize()
                            .fillMaxHeight()
                            .width(itemWidth)
                            .background(
                                color = if (selected) Color(0xFF4A5759) else Color.Transparent,
                                shape = RoundedCornerShape(0.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxHeight()
                        ) {
                            Icon(
                                painter = painterResource(id = screen.iconResId),
                                contentDescription = screen.label,
                                tint = Color.Unspecified
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = screen.label,
                                fontWeight = FontWeight.Medium,
                                fontSize = 12.sp,
                                color = if (selected) Color(0xFFDEDBD2) else Color(0xFF4A5759)
                            )
                        }
                    }
                },

                label = {

                },
                selected = selected,
                onClick = {
                    if (!selected) {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.startDestinationId) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                alwaysShowLabel = false,
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent, // No usar fondo circular
                    selectedIconColor = Color.Black,
                    unselectedIconColor = Color.Black,
                    selectedTextColor = Color(0xFFDEDBD2),
                    unselectedTextColor = Color(0xFF4A5759)
                ),
            )
        }
    }

}
