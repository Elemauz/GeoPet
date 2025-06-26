package com.example.geopet.firebase.model

data class MascotaFirebase(
    val nombre: String = "",
    val rareza: String = "",
    val imagen_url: String = "",
    val lat: Double = 0.0,
    val lon: Double = 0.0,
    val hora: String = "" // hora ISO 8601 o legible
)
