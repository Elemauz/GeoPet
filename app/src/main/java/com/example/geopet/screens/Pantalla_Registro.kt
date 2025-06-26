package com.example.geopet.screens

import androidx.compose.ui.text.input.VisualTransformation
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.geopet.R
import com.example.geopet.navigation.AppScreens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Pantalla_Registro(
                      onRegisterSuccess: () -> Unit, onLoginClick: () -> Unit) {
    Scaffold {
        Contenido_Pantalla_Registro( onRegisterSuccess, onLoginClick)
    }
}

@Composable
fun Contenido_Pantalla_Registro(
                                onRegisterSuccess: () -> Unit,
                                onLoginClick: () -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var apellido by remember { mutableStateOf("") }
    var usuario by remember { mutableStateOf("") }
    var correo by remember { mutableStateOf("") }
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
            Spacer(modifier = Modifier.height(30.dp))

            Image(
                painter = painterResource(id = R.drawable.geopet_logo),
                contentDescription = "Logo GeoPet",
                modifier = Modifier
                    .height(120.dp)
                    .padding(bottom = 16.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            CustomTextField(label = "Nombre", value = nombre, onValueChange = { nombre = it })
            Spacer(modifier = Modifier.height(18.dp))
            CustomTextField(label = "Apellido", value = apellido, onValueChange = { apellido = it })
            Spacer(modifier = Modifier.height(18.dp))
            CustomTextField(label = "Usuario", value = usuario, onValueChange = { usuario = it })
            Spacer(modifier = Modifier.height(18.dp))
            CustomTextField(label = "Correo electrónico", value = correo, onValueChange = { correo = it })
            Spacer(modifier = Modifier.height(18.dp))


            CustomTextField(
                label = "Contraseña",
                value = contrasena,
                onValueChange = { contrasena = it },
                isPassword = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (nombre.isNotBlank() && apellido.isNotBlank() && usuario.isNotBlank() && correo.isNotBlank() && contrasena.isNotBlank()) {
                        onRegisterSuccess()
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
                Text("Registrarse", fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row {
                Text("¿Ya tienes una cuenta?", color = Color.White)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Iniciar sesión",
                    color = CremaClaro,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable {
                        onLoginClick()
                    }
                )
            }

        }
    }
}

@Composable
fun CustomTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(label, color = Color.White) },
        singleLine = true,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
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
}
