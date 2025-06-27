package com.example.geopet.firebase.data

import android.content.Context
import android.util.Log
import com.example.geopet.firebase.model.MascotaFirebase
import com.example.geopet.data.api.RetrofitInstance
import com.example.geopet.data.model.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.util.*
import kotlin.math.*

object MascotaCollectionManager {

    private const val RADIO_KM = 2
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun recolectarMascotasCercanas(
        context: Context,
        mascotas: List<Pet>,
        ubicacionUsuarioLat: Double,
        ubicacionUsuarioLon: Double
    ) {
        val usuarioId = auth.currentUser?.uid
        if (usuarioId == null) {
            Log.e("RecolectarMascotas", "Usuario no autenticado")
            return
        }

        Log.d("RecolectarMascotas", "Usuario $usuarioId ubicado en $ubicacionUsuarioLat,$ubicacionUsuarioLon")
        Log.d("RecolectarMascotas", "Analizando ${mascotas.size} mascotas...")

        withContext(Dispatchers.IO) {
            for (mascota in mascotas) {
                val distancia = calcularDistanciaEnKm(
                    ubicacionUsuarioLat, ubicacionUsuarioLon,
                    mascota.lat, mascota.lon
                )

                Log.d("RecolectarMascotas", "Mascota ${mascota.nombre} está a $distancia km")

                if (distancia <= RADIO_KM) {
                    Log.d("RecolectarMascotas", "→ Dentro del rango")

                    val yaRecolectada = verificarMascotaExistente(usuarioId, mascota.lat, mascota.lon)
                    if (yaRecolectada) {
                        Log.d("RecolectarMascotas", "→ Ya fue recolectada antes. Ignorando.")
                        continue
                    }

                    try {
                        guardarMascotaEnFirestore(usuarioId, mascota)
                        eliminarMascotaDelBackend(mascota.lat, mascota.lon)
                        Log.d("MascotaRecolectada", "→ Recolectada: ${mascota.nombre}")
                    } catch (e: Exception) {
                        Log.e("RecolectarMascotas", "Error en recolección: ${e.localizedMessage}")
                    }
                } else {
                    Log.d("RecolectarMascotas", "→ Fuera del rango")
                }
            }
        }
    }


    private suspend fun verificarMascotaExistente(
        usuarioId: String,
        lat: Double,
        lon: Double
    ): Boolean {
        val snapshot = firestore.collection("usuarios")
            .document(usuarioId)
            .collection("mascotas")
            .whereEqualTo("lat", lat)
            .whereEqualTo("lon", lon)
            .get()
            .await()

        return !snapshot.isEmpty
    }

    private suspend fun guardarMascotaEnFirestore(usuarioId: String, mascota: Pet) {
        val datos = hashMapOf(
            "nombre" to mascota.nombre,
            "rareza" to mascota.rareza,
            "imagen_url" to mascota.imagen_url,
            "lat" to mascota.lat,
            "lon" to mascota.lon,
            "hora" to Date()
        )

        firestore.collection("usuarios")
            .document(usuarioId)
            .collection("mascotas")
            .add(datos)
            .await()
    }
/*
    private suspend fun eliminarMascotaDelBackend(lat: Double, lon: Double) {
        try {
            withContext(Dispatchers.IO) {
                Log.d("EliminarMascota", "Eliminando mascota en $lat, $lon")
                val response = RetrofitInstance.api.eliminarMascotaPorUbicacion(lat, lon)
                if (response.isSuccessful) {
                    Log.d("EliminarMascota", "→ Eliminación exitosa")
                } else {
                    Log.e("EliminarMascota", "→ Falló con código: ${response.code()}, body: ${response.errorBody()?.string()}")
                }
            }
        } catch (e: Exception) {
            Log.e("EliminarMascota", "Error al eliminar mascota: ${e.message}")
        }
    }
*/
private suspend fun eliminarMascotaDelBackend(lat: Double, lon: Double) {
    Log.d("EliminarMascota", "→ Intentando eliminar mascota en $lat, $lon") // Agrega esto

    try {
        withContext(Dispatchers.IO) {
            RetrofitInstance.api.eliminarMascotaPorUbicacion(lat, lon)
        }
        Log.d("EliminarMascota", "→ Eliminación completada") // Y esto
    } catch (e: Exception) {
        Log.e("EliminarMascota", "Error al eliminar mascota: ${e.message}")
    }
}
    suspend fun obtenerMascotasDelUsuario(): List<MascotaFirebase> {
        val usuarioId = auth.currentUser?.uid ?: return emptyList()
        return try {
            val snapshot = firestore.collection("usuarios")
                .document(usuarioId)
                .collection("mascotas")
                .get()
                .await()

            snapshot.documents.mapNotNull { it.toObject(MascotaFirebase::class.java) }

        } catch (e: Exception) {
            Log.e("FirestoreMascotas", "Error: ${e.message}")
            emptyList()
        }
    }

    private fun calcularDistanciaEnKm(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371.0 // Radio de la Tierra en kilómetros
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) +
                cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c // en kilómetros
    }

}
