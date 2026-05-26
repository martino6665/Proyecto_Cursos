package com.example.appcursos.Proyecto.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.appcursos.Proyecto.data.CursoCreate
import com.example.appcursos.Proyecto.viewmodel.ProfesorViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditarCursoScreen(
    cursoId: Int,
    viewModel: ProfesorViewModel,
    onEdicionCompleta: () -> Unit
) {
    val cursoActual = viewModel.listaCursosAsignados.find { it.id == cursoId }

    if (cursoActual == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }
    val maestroId = cursoActual.id_del_profesor
    var subPestanaSeleccionada by remember { mutableIntStateOf(0) }
    var mostrarConfirmacionBorrado by remember { mutableStateOf(false) }
    var mostrarModalNuevaActividad by remember { mutableStateOf(false) }

    // Estados para nueva actividad
    var tituloActividad by remember { mutableStateOf("") }
    var descActividad by remember { mutableStateOf("") }
    var puntosActividad by remember { mutableStateOf("100") }
    var fechaInicioAct by remember { mutableStateOf("") }
    var fechaFinAct by remember { mutableStateOf("") }

    var mostrarPickerInicioAct by remember { mutableStateOf(false) }
    var mostrarPickerFinAct by remember { mutableStateOf(false) }

    val stateInicioAct = rememberDatePickerState()
    val stateFinAct = rememberDatePickerState()
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") } }
    // Carga de pestañas
    LaunchedEffect(subPestanaSeleccionada) {
        when (subPestanaSeleccionada) {
            1 -> viewModel.cargarAlumnosInscritosAlCurso(cursoId)
            2 -> viewModel.cargarActividadesDelCurso(cursoId)
        }
    }

    // Campos de edición
    var nombreCurso by remember { mutableStateOf(cursoActual.nombre_del_curso) }
    var descripcion by remember { mutableStateOf(cursoActual.descripcion ?: "") }

    Scaffold(
        topBar = {
            Column {
                TopAppBar(title = { Text("Gestión: ${cursoActual.nombre_del_curso}", fontWeight = FontWeight.Bold) })
                TabRow(selectedTabIndex = subPestanaSeleccionada) {
                    Tab(selected = subPestanaSeleccionada == 0, onClick = { subPestanaSeleccionada = 0 }, text = { Text("Ajustes") })
                    Tab(selected = subPestanaSeleccionada == 1, onClick = { subPestanaSeleccionada = 1 }, text = { Text("Alumnos") })
                    Tab(selected = subPestanaSeleccionada == 2, onClick = { subPestanaSeleccionada = 2 }, text = { Text("Actividades") })
                }
            }
        },
        floatingActionButton = {
            if (subPestanaSeleccionada == 2) {
                FloatingActionButton(onClick = { mostrarModalNuevaActividad = true }) {
                    Icon(Icons.Default.Add, contentDescription = "Añadir Actividad")
                }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (subPestanaSeleccionada) {
                0 -> {
                    LazyColumn(modifier = Modifier.fillMaxSize().padding(24.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        item { OutlinedTextField(value = nombreCurso, onValueChange = { nombreCurso = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth()) }
                        item { OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth()) }

                        item {
                            Button(
                                onClick = {
                                    viewModel.editarCursoExistente(cursoId, maestroId, CursoCreate(nombreCurso, maestroId, descripcion, cursoActual.fecha_de_inicio.toString(), cursoActual.fecha_de_fin.toString(), cursoActual.color_banner ?: "#3F51B5")) { onEdicionCompleta() }
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) { Text("Guardar Cambios") }
                        }

                        item {
                            Button(
                                onClick = { mostrarConfirmacionBorrado = true },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(Icons.Default.Delete, null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Eliminar Curso")
                            }
                        }
                    }
                }
                1 -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(viewModel.listaAlumnosInscritosCurso) { alumno ->
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                ListItem(headlineContent = { Text("${alumno.nombre} ${alumno.apellido_paterno}") }, leadingContent = { Icon(Icons.Default.Person, null) })
                            }
                        }
                    }
                }
                2 -> {
                    LazyColumn(contentPadding = PaddingValues(16.dp)) {
                        items(items = viewModel.listaActividadesCurso, key = { it.id }) { act ->
                            Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(act.titulo, fontWeight = FontWeight.Bold)
                                    Text(act.descripcion ?: "")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    // --- DIÁLOGO COMPLETO Y FUNCIONAL ---
    if (mostrarModalNuevaActividad) {
        AlertDialog(
            onDismissRequest = { mostrarModalNuevaActividad = false },
            title = { Text("Asignar Actividad", fontWeight = FontWeight.Bold) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp), modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = tituloActividad,
                        onValueChange = { tituloActividad = it },
                        label = { Text("Título") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = descActividad,
                        onValueChange = { descActividad = it },
                        label = { Text("Descripción") },
                        modifier = Modifier.fillMaxWidth()
                    )
                    OutlinedTextField(
                        value = puntosActividad,
                        onValueChange = { puntosActividad = it },
                        label = { Text("Puntos Máximos") },
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Campo Inicio (con DatePicker)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = fechaInicioAct,
                            onValueChange = {},
                            label = { Text("Fecha Inicio") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = { Icon(Icons.Default.DateRange, null) }
                        )
                        Box(Modifier.matchParentSize().clickable { mostrarPickerInicioAct = true })
                    }

                    // Campo Límite (con DatePicker)
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = fechaFinAct,
                            onValueChange = {},
                            label = { Text("Fecha Límite") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            trailingIcon = { Icon(Icons.Default.DateRange, null) }
                        )
                        Box(Modifier.matchParentSize().clickable { mostrarPickerFinAct = true })
                    }
                }
            },
            confirmButton = {
                Button(onClick = {
                    val puntosFloat = puntosActividad.toFloatOrNull() ?: 0f

                    viewModel.crearActividad(
                        cursoId = cursoId,
                        titulo = tituloActividad,
                        descripcion = descActividad,
                        puntos = puntosFloat,
                        inicio = fechaInicioAct,
                        limite = fechaFinAct,
                        onExito = {
                            mostrarModalNuevaActividad = false
                            tituloActividad = ""
                            descActividad = ""
                        }
                    )
                }) {
                    Text("Publicar")
                }
            },
            dismissButton = {
                TextButton(onClick = { mostrarModalNuevaActividad = false }) { Text("Cancelar") }
            }
        )
    }

    // Asegúrate de tener los DatePickers definidos justo después de esto (como ya los tenías)
    if (mostrarPickerInicioAct) {
        DatePickerDialog(onDismissRequest = { mostrarPickerInicioAct = false }, confirmButton = {
            TextButton(onClick = { stateInicioAct.selectedDateMillis?.let { fechaInicioAct = dateFormat.format(Date(it)) }; mostrarPickerInicioAct = false }) { Text("OK") }
        }) { DatePicker(state = stateInicioAct) }
    }
    if (mostrarPickerFinAct) {
        DatePickerDialog(onDismissRequest = { mostrarPickerFinAct = false }, confirmButton = {
            TextButton(onClick = { stateFinAct.selectedDateMillis?.let { fechaFinAct = dateFormat.format(Date(it)) }; mostrarPickerFinAct = false }) { Text("OK") }
        }) { DatePicker(state = stateFinAct) }
    }

    // Diálogo de eliminación
    if (mostrarConfirmacionBorrado) {
        AlertDialog(
            onDismissRequest = { mostrarConfirmacionBorrado = false },
            title = { Text("¿Eliminar curso?") },
            text = { Text("Esta acción es irreversible. Se borrarán todos los datos del curso \"${cursoActual.nombre_del_curso}\".") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.borrarCurso(cursoId, maestroId)
                        onEdicionCompleta()
                    },
                    colors = ButtonDefaults.textButtonColors(contentColor = MaterialTheme.colorScheme.error)
                ) { Text("Eliminar", fontWeight = FontWeight.Bold) }
            },
            dismissButton = { TextButton(onClick = { mostrarConfirmacionBorrado = false }) { Text("Cancelar") } }
        )
    }
}