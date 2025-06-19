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
    @GET("mascotas/todos")      // Obtener todo
    suspend fun getPets(): List<Pet>

    @GET("mascotas")            // Obtener por lugar(ciudad)
    suspend fun getPetsByLocation(@Query("ubicacion") ubicacion: String): List<Pet>

    @POST("ubicacion")          // ENviar la Ubicacion
    suspend fun enviarUbicacion(@Body ubicacion: UbicacionRequest)
}


/*//llamar endpoint POST/ubicacion
LaunchedEffect(locationState) {
    locationState?.let { location ->
        centerMapOnLocation(location, cameraPositionState, scope)

        try {
            val ubicacion = UbicacionRequest(lat = location.latitude, lon = location.longitude)
            RetrofitInstance.api.enviarUbicacion(ubicacion)
            Log.d("Pantalla_Inicio", "Ubicación enviada exitosamente")
        } catch (e: Exception) {
            Log.e("Pantalla_Inicio", "Error al enviar ubicación: ${e.localizedMessage}")
        }
    }
}
*/