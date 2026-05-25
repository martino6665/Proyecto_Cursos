package com.example.appcursos.Proyecto.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.appcursos.Proyecto.viewmodel.AlumnoViewModel
import com.example.appcursos.ui.theme.AppCursosTheme

@Composable
fun PantallaUsuario(
    navController: NavHostController,
    viewModel: AlumnoViewModel = viewModel(),
    alumnoId: Int
) {
    var pestanaSeleccionada by remember { mutableIntStateOf(0) }

    LaunchedEffect(pestanaSeleccionada) {
        when (pestanaSeleccionada) {
            0 -> viewModel.cargarAgendaEscolarEstudiante(alumnoId)
            1 -> viewModel.cargarCursosDelAlumno(alumnoId)
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(tonalElevation = 8.dp) {
                NavigationBarItem(
                    selected = pestanaSeleccionada == 0,
                    onClick = { pestanaSeleccionada = 0 },
                    label = { Text("Inicio", fontWeight = FontWeight.Medium) },
                    icon = { Icon(Icons.Default.Home, contentDescription = "Inicio") }
                )
                NavigationBarItem(
                    selected = pestanaSeleccionada == 1,
                    onClick = { pestanaSeleccionada = 1 },
                    label = { Text("Mis Cursos", fontWeight = FontWeight.Medium) },
                    icon = { Icon(Icons.Default.Book, contentDescription = "Cursos") }
                )
                NavigationBarItem(
                    selected = pestanaSeleccionada == 2,
                    onClick = { pestanaSeleccionada = 2 },
                    label = { Text("Perfil", fontWeight = FontWeight.Medium) },
                    icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") }
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            when (pestanaSeleccionada) {
                0 -> ContenidoInicio(viewModel)
                1 -> ContenidoCursos(viewModel, alumnoId)
                // CORRECCIÓN: Llamada exacta con los 3 parámetros que requiere ContenidoPerfil
                2 -> ContenidoPerfil(navController, "Estudiante", "@usuario • Estudiante Oficial")
            }
        }
    }
}

// --- 📅 INICIO: CALENDARIO DE ACTIVIDADES REALES Y DINÁMICAS ---
@Composable
fun ContenidoInicio(viewModel: AlumnoViewModel) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        item {
            Text(
                text = "Mi Agenda Escolar",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        item {
            val pendientes = viewModel.listaAgendaEstudiante.count { it.nota_obtenida == null }

            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (pendientes == 0)
                        MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)
                    else MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.15f)
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(
                                if (pendientes == 0) MaterialTheme.colorScheme.primary
                                else MaterialTheme.colorScheme.error,
                                CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            if (pendientes == 0) Icons.Default.CheckCircle else Icons.Default.NotificationImportant,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            if (pendientes == 0) "¡Estás al día!" else "Actividades en curso",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            if (pendientes == 0) "No tienes tareas pendientes de revisión por tus profesores."
                            else "Tienes $pendientes actividad(es) enviada(s) esperando calificación en Render.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        item {
            Text(
                text = "Próximas Actividades en el Calendario",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        if (viewModel.cargando) {
            item {
                Box(modifier = Modifier.fillMaxWidth().padding(vertical = 30.dp), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(strokeWidth = 3.dp)
                }
            }
        } else if (viewModel.listaAgendaEstudiante.isEmpty()) {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Text(
                        text = "No se encontraron actividades registradas para tus cursos activos en Render.",
                        modifier = Modifier.padding(24.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            items(viewModel.listaAgendaEstudiante) { entrega ->
                val estaCalificada = entrega.nota_obtenida != null

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(12.dp)
                                .background(
                                    if (estaCalificada) Color(0xFF2E7D32) else Color(0xFFFF9800),
                                    CircleShape
                                )
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1.1f)) {
                            Text(
                                text = "Actividad ID: #${entrega.actividad_id}",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Mi Envío: \"${entrega.contenido_entrega}\"",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 1
                            )

                            if (estaCalificada) {
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Retroalimentación: ${entrega.comentario_profesor ?: "Sin comentarios."}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.primary,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    if (estaCalificada) Color(0xFFE8F5E8) else MaterialTheme.colorScheme.surfaceVariant
                                )
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = if (estaCalificada) "${entrega.nota_obtenida?.toInt()}" else "Pnd",
                                fontWeight = FontWeight.Bold,
                                color = if (estaCalificada) Color(0xFF2E7D32) else MaterialTheme.colorScheme.outline,
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ContenidoCursos(viewModel: AlumnoViewModel, alumnoId: Int) {
    var mostrarModalInscripcion by remember { mutableStateOf(false) }
    var mostrarConfirmacion by remember { mutableStateOf(false) }
    var cursoSeleccionadoId by remember { mutableIntStateOf(0) }
    var cursoSeleccionadoNombre by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                "Mis Cursos",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.ExtraBold
            )
            Button(
                onClick = {
                    viewModel.cargarTodosLosCursosDisponibles()
                    mostrarModalInscripcion = true
                },
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.Add, null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text("Unirme", fontWeight = FontWeight.Bold)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AnimatedVisibility(
            visible = viewModel.mensajeStatus.isNotEmpty(),
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (viewModel.mensajeStatus.contains("exitosa", true))
                        Color(0xFFE8F5E9) else Color(0xFFFFEBEE)
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text(
                    text = viewModel.mensajeStatus,
                    color = if (viewModel.mensajeStatus.contains("exitosa", true))
                        Color(0xFF2E7D32) else Color(0xFFC62828),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(14.dp),
                    fontSize = 14.sp
                )
            }
        }

        if (viewModel.cargando && !mostrarModalInscripcion) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(strokeWidth = 3.dp)
            }
        } else if (viewModel.listaCursosInscritos.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize().padding(bottom = 40.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        Icons.Default.MenuBook,
                        null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        "Aún no estás inscrito en ninguna materia.\nUsa el botón de arriba para unirte a una.",
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.outline
                    )
                }
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(viewModel.listaCursosInscritos) { curso ->
                    val colorHex = curso.color_banner ?: "#3F51B5"

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        shape = RoundedCornerShape(20.dp)
                    ) {
                        Column {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(Color(android.graphics.Color.parseColor(colorHex)))
                                    .padding(horizontal = 20.dp, vertical = 24.dp)
                            ) {
                                Text(
                                    text = curso.nombre_del_curso,
                                    style = MaterialTheme.typography.titleLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                            Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                    text = curso.descripcion,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    maxLines = 3
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (mostrarModalInscripcion) {
        AlertDialog(
            onDismissRequest = { mostrarModalInscripcion = false },
            title = { Text("Buscar Materias Disponibles", fontWeight = FontWeight.Bold, style = MaterialTheme.typography.titleLarge) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("Selecciona la materia a la que deseas registrarte de manera oficial:", style = MaterialTheme.typography.bodyMedium)
                    if (viewModel.cargando) {
                        Box(modifier = Modifier.fillMaxWidth().padding(vertical = 20.dp), contentAlignment = Alignment.Center) { CircularProgressIndicator(modifier = Modifier.size(36.dp)) }
                    } else if (viewModel.listaCursosDisponibles.isEmpty()) {
                        Text("No hay materias disponibles en el servidor de Render actualmente.", color = MaterialTheme.colorScheme.outline, textAlign = TextAlign.Center, modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp))
                    } else {
                        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp), modifier = Modifier.heightIn(max = 260.dp)) {
                            items(viewModel.listaCursosDisponibles) { curso ->
                                Card(
                                    modifier = Modifier.fillMaxWidth().clickable { cursoSeleccionadoId = curso.id; cursoSeleccionadoNombre = curso.nombre_del_curso; mostrarConfirmacion = true },
                                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f)),
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(curso.nombre_del_curso, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.bodyLarge)
                                        Text("ID Curso: ${curso.id} • Servidor Estable", fontSize = 12.sp, color = MaterialTheme.colorScheme.outline)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { mostrarModalInscripcion = false }) { Text("Cerrar", fontWeight = FontWeight.Bold) } }
        )
    }

    if (mostrarConfirmacion) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacion = false },
            icon = { Icon(Icons.Default.Warning, contentDescription = null, tint = MaterialTheme.colorScheme.primary) },
            title = { Text("Confirmar Inscripción", fontWeight = FontWeight.Bold) },
            text = { Text(text = "¿Estás seguro de que quieres unirte de forma oficial al curso \"$cursoSeleccionadoNombre\"?", style = MaterialTheme.typography.bodyMedium, textAlign = TextAlign.Center) },
            confirmButton = { Button(onClick = { viewModel.inscribirseACurso(alumnoId, cursoId = cursoSeleccionadoId) { viewModel.cargarCursosDelAlumno(alumnoId) }; mostrarConfirmacion = false; mostrarModalInscripcion = false }) { Text("Sí, unirme", fontWeight = FontWeight.Bold) } },
            dismissButton = { TextButton(onClick = { mostrarConfirmacion = false }) { Text("No, cancelar", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.error) } }
        )
    }
}

@Composable
fun ContenidoPerfil(navController: NavHostController, nombreEstudiante: String, usuarioTag: String) {
    Column(
        modifier = Modifier.fillMaxSize().padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Box(modifier = Modifier.size(100.dp).background(Brush.linearGradient(colors = listOf(MaterialTheme.colorScheme.primary, MaterialTheme.colorScheme.tertiary)), CircleShape), contentAlignment = Alignment.Center) {
            Icon(Icons.Default.Person, null, modifier = Modifier.size(54.dp), tint = Color.White)
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = nombreEstudiante, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onBackground)
            Text(text = usuarioTag, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.outline, fontWeight = FontWeight.Medium)
        }
        HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = MaterialTheme.colorScheme.surfaceVariant)
        Button(
            onClick = { navController.navigate("login") { popUpTo("dashboard") { inclusive = true } } },
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
            modifier = Modifier.fillMaxWidth().height(54.dp),
            shape = RoundedCornerShape(16.dp),
            elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
        ) {
            Icon(Icons.Default.ExitToApp, null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Cerrar Sesión Escolar", fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }
    }
}