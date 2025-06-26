package com.example.geopet.firebase.auth

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.example.geopet.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object GoogleAuthManager {
    fun getGoogleSignInClient(context: Context): GoogleSignInClient {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        return GoogleSignIn.getClient(context, gso)
    }

    suspend fun firebaseAuthWithGoogle(idToken: String, context: Context): Boolean {
        return try {
            val credential = GoogleAuthProvider.getCredential(idToken, null)
            val result = FirebaseAuth.getInstance().signInWithCredential(credential).await()
            val user = result.user

            if (user != null) {
                true
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Fallo al obtener el usuario de Google", Toast.LENGTH_LONG).show()
                }
                false
            }
        } catch (e: Exception) {
            Log.e("GoogleAuthManager", "Error autenticando con Firebase", e)
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Error al autenticar con Google", Toast.LENGTH_LONG).show()
            }
            false
        }
    }
}
