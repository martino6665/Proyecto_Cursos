package com.example.appcursos.Proyecto.composables

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appcursos.ui.theme.AppCursosTheme

@Composable
fun PantallaUsuario(navController: NavHostController) {
    var pestañaSeleccionada by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = pestañaSeleccionada == 0,
                    onClick = { pestañaSeleccionada = 0 },
                    label = { Text("Inicio") },
                    icon = { Icon(Icons.Default.Home, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = pestañaSeleccionada == 1,
                    onClick = { pestañaSeleccionada = 1 },
                    label = { Text("Cursos") },
                    icon = { Icon(Icons.Default.Book, contentDescription = null) }
                )
                NavigationBarItem(
                    selected = pestañaSeleccionada == 2,
                    onClick = { pestañaSeleccionada = 2 },
                    label = { Text("Perfil") },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) }
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            when (pestañaSeleccionada) {
                0 -> ContenidoInicio()
                1 -> ContenidoCursos()
                2 -> ContenidoPerfil(navController)
            }
        }
    }
}

@Composable fun ContenidoInicio() {
    Text("Dashboard VisionEducation", style = MaterialTheme.typography.headlineMedium)
}

@Composable fun ContenidoCursos() {
    Text("Mis Cursos", style = MaterialTheme.typography.headlineMedium)
}

@Composable fun ContenidoPerfil(navController: NavHostController) {
    Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium)
    Spacer(modifier = Modifier.height(16.dp))

    // --- CORRECCIÓN DE SEGURIDAD (PARTE 3) ---
    Button(
        onClick = {
            // Al navegar a "inicio", borramos todo rastro del dashboard
            navController.navigate("inicio") {
                popUpTo("dashboard") {
                    inclusive = true // Esto elimina el dashboard del historial
                }
                launchSingleTop = true // Evita duplicar la pantalla de inicio
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        )
    ) {
        Text("Cerrar Sesión")
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewUsuario() {
    AppCursosTheme {
        PantallaUsuario(rememberNavController())
    }
}