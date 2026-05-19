package com.example.appcursos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

// --- IMPORTS UNIFICADOS ---
import com.example.appcursos.Proyecto.composables.PantallaInicio
import com.example.appcursos.Proyecto.composables.PantallaIniciarSesion
import com.example.appcursos.Proyecto.composables.PantallaCrearPerfilNuevo
import com.example.appcursos.Proyecto.composables.PantallaUsuario
import com.example.appcursos.Proyecto.view.GestionCursosScreen
import com.example.appcursos.Proyecto.viewmodel.LoginViewModel
import com.example.appcursos.Proyecto.viewmodel.RegistroViewModel
import com.example.appcursos.Proyecto.viewmodel.ProfesorViewModel
import com.example.appcursos.Proyecto.viewmodel.AlumnoViewModel
import com.example.appcursos.ui.theme.AppCursosTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AppCursosTheme {
                val navController = rememberNavController()

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "inicio",
                        modifier = Modifier.padding(innerPadding)
                    ) {
                        // 1. Bienvenida
                        composable("inicio") {
                            PantallaInicio(navController = navController)
                        }

                        // 2. Login (Inyectamos su ViewModel de forma segura)
                        composable("login") {
                            val loginViewModel: LoginViewModel = viewModel()
                            PantallaIniciarSesion(
                                navController = navController,
                                viewModel = loginViewModel
                            )
                        }

                        // 3. Registro (Inyectamos su ViewModel de forma segura)
                        composable("registro") {
                            val registroViewModel: RegistroViewModel = viewModel()
                            PantallaCrearPerfilNuevo(
                                navController = navController,
                                viewModel = registroViewModel
                            )
                        }

                        // 4. Dashboard Alumno
                        composable("dashboard") {
                            val alumnoViewModel: AlumnoViewModel = viewModel()
                            PantallaUsuario(
                                navController = navController,
                                viewModel = alumnoViewModel
                            )
                        }

                        // 5. Gestión Profesor
                        composable(
                            route = "gestion_cursos/{maestroId}",
                            arguments = listOf(navArgument("maestroId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val maestroId = backStackEntry.arguments?.getInt("maestroId") ?: 0
                            val profesorViewModel: ProfesorViewModel = viewModel()

                            GestionCursosScreen(
                                maestroId = maestroId,
                                viewModel = profesorViewModel,
                                onCrearCurso = {},
                                onEditarCurso = {}
                            )
                        }
                    }
                }
            }
        }
    }
}