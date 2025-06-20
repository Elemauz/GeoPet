package com.example.geopet.data.api

import com.example.geopet.data.model.Pet
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

data class UbicacionRequest(
    val lat: Double,
    val lon: Double
)

interface PetApiService {
    @GET("mascotas/todos")      // Obtener todas las mascotas
    suspend fun getAllPets(): List<Pet>

    @GET("mascotas")            // Obtener por lugar (ciudad)
    suspend fun getPetsByLocation(@Query("ubicacion") ubicacion: String): List<Pet>

    @POST("ubicacion")          // Enviar ubicación del usuario
    suspend fun enviarUbicacion(@Body ubicacion: UbicacionRequest)
}