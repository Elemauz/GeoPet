package com.example.geopet.firebase.data

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.geopet.NotificationHelper
import com.example.geopet.data.api.RetrofitInstance
import com.example.geopet.data.model.Pet
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.tasks.await
import java.util.*
import kotlin.math.*


object MascotaCollectionManager {

    private const val RADIO_METROS = 500.0
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    suspend fun recolectarMascotasCercanas(
        context: Context,
        mascotas: List<Pet>,
        ubicacionUsuarioLat: Double,
        ubicacionUsuarioLon: Double
    ) {
        val usuarioId = auth.currentUser?.uid ?: return

        for (mascota in mascotas) {
            val distancia = calcularDistanciaEnMetros(
                ubicacionUsuarioLat, ubicacionUsuarioLon,
                mascota.lat, mascota.lon
            )

            if (distancia <= RADIO_METROS) {
                // ⚠️ Evita duplicados (si ya fue recolectada antes)
                val yaRecolectada = verificarMascotaExistente(usuarioId, mascota.lat, mascota.lon)
                if (yaRecolectada) continue

                // 1. Guardar en Firestore
                guardarMascotaEnFirestore(usuarioId, mascota)

                // 2. Eliminar del backend
                eliminarMascotaDelBackend(mascota.lat, mascota.lon)

                // 3. Notificación o log
                Log.d("MascotaRecolectada", "Recolectada: ${mascota.nombre}")
                Toast.makeText(context, "¡Recolectaste a ${mascota.nombre}!", Toast.LENGTH_SHORT).show()
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

    private suspend fun eliminarMascotaDelBackend(lat: Double, lon: Double) {
        try {
            RetrofitInstance.api.eliminarMascotaPorUbicacion(lat, lon)
        } catch (e: Exception) {
            Log.e("EliminarMascota", "Error al eliminar mascota: ${e.message}")
        }
    }

    private fun calcularDistanciaEnMetros(
        lat1: Double, lon1: Double,
        lat2: Double, lon2: Double
    ): Double {
        val R = 6371000.0 // radio Tierra en metros
        val dLat = Math.toRadians(lat2 - lat1)
        val dLon = Math.toRadians(lon2 - lon1)
        val a = sin(dLat / 2).pow(2.0) + cos(Math.toRadians(lat1)) *
                cos(Math.toRadians(lat2)) * sin(dLon / 2).pow(2.0)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }
}
