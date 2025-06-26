package com.example.geopet.screens

import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.geopet.firebase.auth.FirebaseAuthManager
import com.example.geopet.firebase.auth.GoogleAuthManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun PantallaLogin(navController: NavController) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var rememberPassword by remember { mutableStateOf(false) }

    val googleClient = remember { GoogleAuthManager.getGoogleSignInClient(context) }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                val idToken = account.idToken
                scope.launch {
                    val success = GoogleAuthManager.firebaseAuthWithGoogle(idToken.toString(), context)
                    if (success) {
                        navController.navigate("pantalla_inicio")
                    }
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "Error al iniciar sesión con Google", Toast.LENGTH_LONG).show()
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar Sesión", fontSize = 24.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(20.dp))

        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Correo") })
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation()
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = rememberPassword, onCheckedChange = { rememberPassword = it })
            Text("Guardar contraseña")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (email.isBlank() || password.length < 6) {
                Toast.makeText(context, "Ingrese un correo válido y una contraseña de al menos 6 caracteres", Toast.LENGTH_LONG).show()
            } else {
                scope.launch {
                    val success = FirebaseAuthManager.loginWithEmail(email, password, context)
                    if (success) navController.navigate("pantalla_inicio")
                }
            }
        }) {
            Text("Ingresar")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            "¿No tienes cuenta? Regístrate",
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.clickable {
                navController.navigate("pantalla_registro")
            }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = {
            launcher.launch(googleClient.signInIntent)
        }) {
            Text("Iniciar sesión con Google")
        }

        Button(onClick = {
            Toast.makeText(context, "Facebook aún no implementado", Toast.LENGTH_SHORT).show()
        }) {
            Text("Iniciar sesión con Facebook")
        }
    }
}
