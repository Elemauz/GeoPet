package com.example.geopet.screens

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.geopet.R
import com.example.geopet.firebase.auth.FirebaseAuthManager
import com.example.geopet.firebase.auth.GoogleAuthManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.common.api.ApiException
import kotlinx.coroutines.launch

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Login(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    Scaffold {
        Contenido_Pantalla_Login(onLoginSuccess, onRegisterClick)
    }
}

@Composable
fun Contenido_Pantalla_Login(onLoginSuccess: () -> Unit, onRegisterClick: () -> Unit) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val googleClient = remember { GoogleAuthManager.getGoogleSignInClient(context) }

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                    if (success) onLoginSuccess()
                }
            } catch (e: ApiException) {
                Toast.makeText(context, "Error al iniciar sesión con Google", Toast.LENGTH_LONG).show()
            }
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.login_bg),
            contentDescription = "Fondo",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Image(
                painter = painterResource(id = R.drawable.geopet_logo),
                contentDescription = "Logo GeoPet",
                modifier = Modifier
                    .height(140.dp)
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            TextField(
                value = email,
                onValueChange = { email = it },
                placeholder = { Text("Correo", color = Color.White) },
                singleLine = true,
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = VerdeOscuro.copy(alpha = 0.3f),
                    unfocusedContainerColor = VerdeOscuro.copy(alpha = 0.3f),
                    disabledContainerColor = VerdeOscuro.copy(alpha = 0.2f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.LightGray,
                    cursorColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = { password = it },
                placeholder = { Text("Contraseña", color = Color.White) },
                singleLine = true,
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(20.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = VerdeOscuro.copy(alpha = 0.3f),
                    unfocusedContainerColor = VerdeOscuro.copy(alpha = 0.3f),
                    disabledContainerColor = VerdeOscuro.copy(alpha = 0.2f),
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.LightGray,
                    cursorColor = Color.White
                ),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.isBlank() || password.length < 6) {
                        Toast.makeText(
                            context,
                            "Ingrese un correo válido y una contraseña de al menos 6 caracteres",
                            Toast.LENGTH_LONG
                        ).show()
                    } else {
                        scope.launch {
                            val success = FirebaseAuthManager.loginWithEmail(email, password, context)
                            
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = VerdeClaro,
                    contentColor = VerdeOscuro
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Iniciar Sesión", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón "Continuar con Google"
            Button(
                onClick = {
                    launcher.launch(googleClient.signInIntent)
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = Color.Unspecified
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(60.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.android_light_rd_ctn),
                    contentDescription = "Google Logo",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row {
                Text("¿No tienes cuenta?", color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Crear cuenta",
                    color = CremaClaro,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onRegisterClick()
                    }
                )
            }
        }
    }
}
