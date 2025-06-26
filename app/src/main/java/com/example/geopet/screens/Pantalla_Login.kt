package com.example.geopet.screens

import android.annotation.SuppressLint
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.geopet.R


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Login(onLoginSuccess: () -> Unit,  onRegisterClick: () -> Unit) {
    Scaffold {
        Contenido_Pantalla_Login(onLoginSuccess,  onRegisterClick)
    }
}

@Composable
fun Contenido_Pantalla_Login(onLoginSuccess: () -> Unit,  onRegisterClick: () -> Unit) {
    var usuario by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }

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
                value = usuario,
                onValueChange = { usuario = it },
                placeholder = { Text("Usuario", color = Color.White) },
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
                value = contrasena,
                onValueChange = { contrasena = it },
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
                    // Validación de login (simulada aquí)
                    if (usuario.isNotBlank() && contrasena.isNotBlank()) {
                        onLoginSuccess()
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
            // Botón solo con el logo XML (sin texto ni contenido adicional)
            Button(
                onClick = {
                    // Acción: continuar con Google
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
