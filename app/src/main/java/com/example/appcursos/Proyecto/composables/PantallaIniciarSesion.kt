package com.example.appcursos.Proyecto.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController // NUEVO IMPORT
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appcursos.Proyecto.viewmodel.LoginViewModel
import com.example.appcursos.ui.theme.AppCursosTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PantallaIniciarSesion(
    navController: NavHostController,
    viewModel: LoginViewModel = viewModel()
) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // MEJORA: Controlador nativo para ocultar el teclado antes del salto de pantalla
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
        Column(
            modifier = Modifier.fillMaxSize().padding(horizontal = 30.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "VisionEducation",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )

            Text(
                text = "Ingresa con tu nombre de usuario",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 4.dp, bottom = 40.dp)
            )

            // CAMPO DE USUARIO
            OutlinedTextField(
                value = usuario,
                onValueChange = { usuario = it },
                label = { Text("Usuario") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                enabled = !viewModel.cargando
            )

            Spacer(modifier = Modifier.height(16.dp))

            // CAMPO DE CONTRASEÑA
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Contraseña") },
                leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null) },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                enabled = !viewModel.cargando
            )

            // MOSTRAR ERROR SI EXISTE (Viene de tu servidor en Render)
            if (viewModel.mensajeError.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = viewModel.mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // BOTÓN INTEGRADO CON DOS PARÁMETROS DINÁMICOS
            Button(
                onClick = {
                    // 1. Forzar el cierre del teclado para que no interfiera con la animación de transición
                    keyboardController?.hide()

                    // 2. Disparar el login
                    viewModel.login(usuario, password) { rol, idUsuario ->
                        // CORRECCIÓN: Rutas dinámicas con ID que hacen juego exacto con tu MainActivity.kt
                        if (rol == "profesor") {
                            navController.navigate("gestion_cursos/$idUsuario") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            navController.navigate("dashboard/$idUsuario") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !viewModel.cargando && usuario.isNotBlank() && password.isNotBlank()
            ) {
                if (viewModel.cargando) {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.onPrimary,
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                } else {
                    Text("Entrar", fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }

            TextButton(
                onClick = { navController.navigate("registro") },
                enabled = !viewModel.cargando
            ) {
                Text("¿No tienes cuenta? Regístrate aquí")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewLogin() {
    AppCursosTheme {
        val dummyNavController = rememberNavController()
        PantallaIniciarSesion(navController = dummyNavController)
    }
}