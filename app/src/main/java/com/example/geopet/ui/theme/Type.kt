package com.example.geopet.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.geopet.R

// Set of Material typography styles to start with

val FuenteGeoPet = FontFamily(
    Font(R.font.nunito_bold)
)
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = FuenteGeoPet,
        fontSize = 16.sp
    ),
    // puedes sobreescribir otros estilos si quieres
    titleLarge = TextStyle(
        fontFamily = FuenteGeoPet,
        fontSize = 22.sp
    )
    // agrega otros estilos si necesitas: labelSmall, headlineMedium, etc.
)