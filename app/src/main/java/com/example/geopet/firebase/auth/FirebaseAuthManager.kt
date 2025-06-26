package com.example.geopet.firebase.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.tasks.await

object FirebaseAuthManager {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    // 🔐 Login con correo y contraseña
    suspend fun loginWithEmail(email: String, password: String, context: Context): Boolean {
        return try {
            auth.signInWithEmailAndPassword(email, password).await()
            true
        } catch (e: Exception) {
            Log.e("Auth", "Login error", e)
            Toast.makeText(context, "Error al iniciar sesión", Toast.LENGTH_SHORT).show()
            false
        }
    }

    // 🆕 Registro con correo y contraseña
    suspend fun registerWithEmail(email: String, password: String, context: Context): Boolean {
        return try {
            auth.createUserWithEmailAndPassword(email, password).await()
            Toast.makeText(context, "Cuenta creada correctamente", Toast.LENGTH_SHORT).show()
            true
        } catch (e: Exception) {
            Log.e("Auth", "Registro error", e)
            Toast.makeText(context, "Error al crear la cuenta", Toast.LENGTH_SHORT).show()
            false
        }
    }

    fun getCurrentUser() = auth.currentUser
    fun signOut() = auth.signOut()
}