package com.example.appcursos.Proyecto.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Book
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appcursos.Proyecto.viewmodel.AlumnoViewModel
import com.example.appcursos.ui.theme.AppCursosTheme

@Composable
fun PantallaUsuario(
    navController: NavHostController,
    viewModel: AlumnoViewModel = viewModel() // Integramos el cerebro del Alumno
) {
    var pestañaSeleccionada by remember { mutableIntStateOf(0) }
    val alumnoIdDePrueba = 2 // Simulamos el ID del estudiante tras el login exitoso

    // Monitoreamos si cambia a la pestaña de cursos para descargar los datos reales desde Render
    LaunchedEffect(pestañaSeleccionada) {
        if (pestañaSeleccionada == 1) {
            viewModel.cargarCursosDelAlumno(alumnoIdDePrueba)
        }
    }

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
                1 -> ContenidoCursos(viewModel) // Le pasamos el estado cargado a la sub-pantalla
                2 -> ContenidoPerfil(navController)
            }
        }
    }
}

@Composable
fun ContenidoInicio() {
    Text("Dashboard VisionEducation", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(8.dp))
    Text("Bienvenido al portal oficial de alumnos.", style = MaterialTheme.typography.bodyMedium)
}

// MEJORA: Esta sección ahora lee los datos vivos en línea recta desde tu backend en FastAPI
@Composable
fun ContenidoCursos(viewModel: AlumnoViewModel) {
    Text("Mis Cursos Inscritos", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))

    if (viewModel.mensajeStatus.isNotEmpty()) {
        Text(text = viewModel.mensajeStatus, color = Color.Red, fontWeight = FontWeight.Bold)
    }

    if (viewModel.cargando) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (viewModel.listaCursosInscritos.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Aún no te has inscrito en ninguna materia.")
        }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            items(viewModel.listaCursosInscritos) { curso ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = curso.nombre_del_curso, // Variable idéntica a dtos.py
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = curso.descripcion, // Variable idéntica a dtos.py
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun ContenidoPerfil(navController: NavHostController) {
    Text("Mi Perfil", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
    Spacer(modifier = Modifier.height(16.dp))

    Button(
        onClick = {
            navController.navigate("inicio") {
                popUpTo("dashboard") { inclusive = true }
                launchSingleTop = true
            }
        },
        colors = ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error
        ),
        modifier = Modifier.fillMaxWidth()
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