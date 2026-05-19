package com.example.appcursos.Proyecto.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.appcursos.Proyecto.viewmodel.ProfesorViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GestionCursosScreen(
    maestroId: Int,
    onCrearCurso: () -> Unit,
    onEditarCurso: (Int) -> Unit,
    viewModel: ProfesorViewModel = viewModel()
) {
    // Al cargar la pantalla, mandamos a traer los cursos de este maestro desde Render
    LaunchedEffect(Unit) {
        viewModel.cargarCursos(maestroId)
    }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Gestión de Cursos - Profesor") })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCrearCurso,
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(Icons.Default.Add, contentDescription = "Crear nuevo curso")
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Zona para mostrar avisos o errores en la pantalla
            if (viewModel.mensajeStatus.isNotEmpty()) {
                Text(
                    text = viewModel.mensajeStatus,
                    color = Color.Blue,
                    modifier = Modifier.padding(16.dp),
                    fontWeight = FontWeight.Bold
                )
            }

            if (viewModel.cargando) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else if (viewModel.listaCursos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No tienes cursos asignados actualmente.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(viewModel.listaCursos) { curso ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = curso.nombre_del_curso,
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = curso.descripcion,
                                    style = MaterialTheme.typography.bodyMedium
                                )
                                Spacer(modifier = Modifier.height(12.dp))

                                // Fila de acciones exclusivas del profesor (Editar, Calificar, Unirse)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // 1. Botón Editar
                                    IconButton(onClick = { onEditarCurso(curso.id) }) {
                                        Icon(Icons.Default.Edit, contentDescription = "Editar Curso", tint = Color.Blue)
                                    }

                                    // 2. Botón Calificar Alumno
                                    Button(
                                        onClick = {
                                            // Llama a la función del ViewModel con un ID de alumno de prueba (ej. ID: 1, Nota: 10)
                                            viewModel.ponerCalificacion(alumnoId = 1, cursoId = curso.id, nota = 10)
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
                                    ) {
                                        Icon(Icons.Default.Star, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("Calificar")
                                    }

                                    // 3. Botón Unirse / Inscribirse
                                    OutlinedButton(
                                        onClick = {
                                            viewModel.unirseACurso(maestroId = maestroId, cursoId = curso.id)
                                        }
                                    ) {
                                        Text("Unirme")
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}