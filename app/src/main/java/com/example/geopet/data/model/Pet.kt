package com.example.geopet.data.model

data class Pet(
    val id: Int,
    val nombre: String,
    val rareza: String,
    val imagen_url: String,
    val lat: Double,
    val lon: Double,
    val distancia_km: Double? = null
)
