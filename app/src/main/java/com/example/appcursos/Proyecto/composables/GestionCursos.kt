package com.example.appcursos.Proyecto.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appcursos.Proyecto.viewmodel.ProfesorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionCursosScreen(
    maestroId: Int,
    navController: NavHostController,
    onCrearCurso: () -> Unit,
    onEditarCurso: (Int) -> Unit,
    viewModel: ProfesorViewModel
) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    // Refresca automáticamente al entrar a la pantalla o si el ID cambia
    LaunchedEffect(maestroId) {
        viewModel.cargarMateriasDelProfesor(maestroId)
    }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Panel Maestro: VisionEducation", fontWeight = FontWeight.Bold) }) },
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = pestanaSeleccionada == 0,
                    onClick = { pestanaSeleccionada = 0 },
                    label = { Text("Mis Cursos") },
                    icon = { Icon(Icons.Default.Star, null) }
                )
                NavigationBarItem(
                    selected = pestanaSeleccionada == 1,
                    onClick = { pestanaSeleccionada = 1 },
                    label = { Text("Mi Perfil") },
                    icon = { Icon(Icons.Default.Person, null) }
                )
            }
        },
        floatingActionButton = {
            if (pestanaSeleccionada == 0) {
                FloatingActionButton(onClick = onCrearCurso, containerColor = MaterialTheme.colorScheme.primary) {
                    Icon(Icons.Default.Add, contentDescription = "Crear nuevo curso")
                }
            }
        }
    ) { paddingValues ->
        Box(modifier = Modifier.padding(paddingValues).fillMaxSize()) {
            when (pestanaSeleccionada) {
                0 -> VistaCursosProfesor(viewModel, onEditarCurso)
                1 -> VistaPerfilProfesor(navController)
            }
        }
    }
}

@Composable
fun VistaCursosProfesor(viewModel: ProfesorViewModel, onEditarCurso: (Int) -> Unit) {
    // Observamos la lista directamente desde el ViewModel
    val lista = viewModel.listaCursosAsignados

    when {
        viewModel.cargando -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }
        lista.isEmpty() -> {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No tienes cursos creados todavía.")
            }
        }
        else -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // El 'key' es vital para que Compose sepa que la lista cambió
                items(items = lista, key = { it.id }) { curso ->
                    val colorHex = try { curso.color_banner?.takeIf { it.startsWith("#") } ?: "#3F51B5" } catch (e: Exception) { "#3F51B5" }
                    val backgroundColor = Color(android.graphics.Color.parseColor(colorHex))

                    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)) {
                        Column {
                            Box(modifier = Modifier.fillMaxWidth().background(backgroundColor).padding(20.dp)) {
                                Text(curso.nombre_del_curso, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(curso.descripcion, style = MaterialTheme.typography.bodyMedium)
                                Spacer(modifier = Modifier.height(12.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                    Text("Inicio: ${curso.fecha_de_inicio}", style = MaterialTheme.typography.bodySmall)
                                    Text("Fin: ${curso.fecha_de_fin}", style = MaterialTheme.typography.bodySmall)
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Button(onClick = { onEditarCurso(curso.id) }, modifier = Modifier.fillMaxWidth(), colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)) {
                                    Icon(Icons.Default.Edit, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Administrar Curso")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun VistaPerfilProfesor(navController: NavHostController) {
    Column(modifier = Modifier.fillMaxSize().padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Box(modifier = Modifier.size(90.dp).background(MaterialTheme.colorScheme.primaryContainer, CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Person, null, modifier = Modifier.size(50.dp), tint = MaterialTheme.colorScheme.primary)
        }
        Text("Panel del Profesor", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold)
        HorizontalDivider()
        Button(onClick = { navController.navigate("login") { popUpTo(0) } }, colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error), modifier = Modifier.fillMaxWidth().height(50.dp), shape = RoundedCornerShape(12.dp)) {
            Text("Cerrar Sesión")
        }
    }
}