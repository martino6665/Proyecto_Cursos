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

@Composable
fun PantallaIniciarSesion(
    navController: NavHostController,
    viewModel: LoginViewModel = viewModel()
) {
    var usuario by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

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
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = viewModel.mensajeError,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // BOTÓN MEJORADO: Coincide exactamente con la lógica de Roles
            Button(
                onClick = {
                    // Llamamos al login; el bloque recibe el 'rol' que regresa la base de datos de Render
                    viewModel.login(usuario, password) { rol ->
                        // Discriminamos el destino según lo guardado en el backend
                        if (rol == "profesor") {
                            // Por ahora simulamos pasar un ID fijo o de prueba (ej: 1) para tu ruta del Main
                            navController.navigate("gestion_cursos/1") {
                                popUpTo("login") { inclusive = true }
                            }
                        } else {
                            // Si es alumno, va al contenido general protegido
                            navController.navigate("dashboard") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                shape = RoundedCornerShape(16.dp),
                enabled = !viewModel.cargando
            ) {
                if (viewModel.cargando) {
                    CircularProgressIndicator(
                        color = Color.White,
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