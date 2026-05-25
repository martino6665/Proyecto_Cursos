package com.example.appcursos

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.appcursos.Proyecto.composables.*
import com.example.appcursos.Proyecto.view.*
import com.example.appcursos.Proyecto.viewmodel.*
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
                        // Flujos base
                        composable("inicio") { PantallaInicio(navController) }
                        composable("login") { PantallaIniciarSesion(navController, viewModel()) }
                        composable("registro") { PantallaCrearPerfilNuevo(navController, viewModel()) }

                        // Dashboard Alumno
                        composable(
                            "dashboard/{usuarioId}",
                            arguments = listOf(navArgument("usuarioId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            PantallaUsuario(
                                navController,
                                viewModel(),
                                backStackEntry.arguments?.getInt("usuarioId") ?: 0
                            )
                        }

                        // --- GRAFO MEJORADO: Ruta estática ---
                        navigation(startDestination = "gestion_cursos/{maestroId}", route = "flujo_maestro") {

                            composable(
                                "gestion_cursos/{maestroId}",
                                arguments = listOf(navArgument("maestroId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val maestroId = backStackEntry.arguments?.getInt("maestroId") ?: 0
                                // Obtenemos la instancia del ViewModel compartida del grafo padre
                                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("flujo_maestro") }
                                val viewModel: ProfesorViewModel = viewModel(parentEntry)

                                GestionCursosScreen(
                                    maestroId,
                                    navController,
                                    { navController.navigate("crear_curso/$maestroId") },
                                    { navController.navigate("editar_curso/$it") },
                                    viewModel
                                )
                            }

                            composable(
                                "crear_curso/{maestroId}",
                                arguments = listOf(navArgument("maestroId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val maestroId = backStackEntry.arguments?.getInt("maestroId") ?: 0
                                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("flujo_maestro") }
                                CrearCursoScreen(maestroId, viewModel(parentEntry)) { navController.popBackStack() }
                            }

                            composable(
                                "editar_curso/{cursoId}",
                                arguments = listOf(navArgument("cursoId") { type = NavType.IntType })
                            ) { backStackEntry ->
                                val cursoId = backStackEntry.arguments?.getInt("cursoId") ?: 0
                                val parentEntry = remember(backStackEntry) { navController.getBackStackEntry("flujo_maestro") }
                                EditarCursoScreen(cursoId, viewModel(parentEntry)) { navController.popBackStack() }
                            }
                        }
                    }
                }
            }
        }
    }
}