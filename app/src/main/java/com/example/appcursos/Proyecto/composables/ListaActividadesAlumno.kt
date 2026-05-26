package com.example.appcursos.Proyecto.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.appcursos.Proyecto.viewmodel.AlumnoViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaActividadesAlumno(
    cursoId: Int,
    alumnoId: Int,
    viewModel: AlumnoViewModel
) {
    LaunchedEffect(cursoId) {
        viewModel.cargarActividadesParaAlumno(cursoId)
    }

    Scaffold(
        topBar = {
            // El componente correcto es TopAppBar
            TopAppBar(
                title = { Text("Actividades del Curso") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }
    ) { padding ->
        if (viewModel.cargando) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else if (viewModel.listaActividadesAlumno.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                Text("No hay actividades asignadas.")
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(padding).fillMaxSize(),
                contentPadding = PaddingValues(16.dp)
            ) {
                items(viewModel.listaActividadesAlumno) { actividad ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = actividad.titulo, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleMedium)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = actividad.descripcion ?: "Sin descripción", style = MaterialTheme.typography.bodyMedium)
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = "Puntos: ${actividad.puntos_maximos}", style = MaterialTheme.typography.labelMedium)

                            Button(
                                onClick = {
                                    viewModel.enviarActividadResuelta(
                                        actividad.id, alumnoId, cursoId, "Entrega realizada"
                                    ) { /* Acción extra si fuera necesaria */ }
                                },
                                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
                            ) {
                                Text("Entregar Tarea")
                            }
                        }
                    }
                }
            }
        }
    }
}