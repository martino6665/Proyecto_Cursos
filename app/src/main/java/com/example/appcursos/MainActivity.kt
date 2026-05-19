package com.example.appcursos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// --- REVISIÓN DE IMPORTS ---
import com.example.appcursos.composables.PantallaInicio
import com.example.appcursos.Proyecto.composables.PantallaIniciarSesion
import com.example.appcursos.Proyecto.composables.PantallaCrearPerfilNuevo
import com.example.appcursos.Proyecto.composables.PantallaUsuario
import com.example.appcursos.ui.theme.AppCursosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppCursosTheme {
                // El motor que controla los saltos entre pantallas
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "inicio", // Punto de entrada obligatorio
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Pantalla de Bienvenida (Logo y elección)
                        composable("inicio") {
                            PantallaInicio(navController = navController)
                        }

                        // 2. Aduana de Seguridad (Pide credenciales a Render)
                        composable("login") {
                            // Aquí se inyecta automáticamente el LoginViewModel
                            PantallaIniciarSesion(navController = navController)
                        }

                        // 3. Registro de nuevos usuarios (Alumno/Maestro)
                        composable("registro") {
                            PantallaCrearPerfilNuevo(navController = navController)
                        }

                        // 4. Contenido Protegido (Solo se llega aquí tras el Login exitoso)
                        composable("dashboard") {
                            PantallaUsuario(navController = navController)
                        }
                    }
                }
            }
        }
    }
}