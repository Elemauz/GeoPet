package com.example.geopet.data.api

import com.example.geopet.data.model.Pet
import retrofit2.http.GET

interface PetApiService {
    @GET("mascotas")
    suspend fun getPets(): List<Pet>
}
