package com.example.appcursos.Proyecto.view

import androidx.compose.foundation.background
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
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        return
    }

    val maestroId = cursoActual.id_del_profesor
    var subPestanaSeleccionada by remember { mutableIntStateOf(0) }

    // Sincronización de datos al cambiar de pestaña
    LaunchedEffect(subPestanaSeleccionada) {
        when (subPestanaSeleccionada) {
            1 -> viewModel.cargarAlumnosInscritosAlCurso(cursoId)
            2 -> viewModel.cargarActividadesDelCurso(cursoId)
        }
    }

    var nombreCurso by remember { mutableStateOf(cursoActual.nombre_del_curso) }
    var descripcion by remember { mutableStateOf(cursoActual.descripcion) }
    var fechaInicio by remember { mutableStateOf(cursoActual.fecha_de_inicio ?: "") }
    var fechaFin by remember { mutableStateOf(cursoActual.fecha_de_fin ?: "") }

    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }

    var mostrarModalNuevaActividad by remember { mutableStateOf(false) }
    var tituloActividad by remember { mutableStateOf("") }
    var descActividad by remember { mutableStateOf("") }
    var puntosActividad by remember { mutableStateOf("100") }
    var fechaInicioAct by remember { mutableStateOf("") }
    var fechaFinAct by remember { mutableStateOf("") }

    var mostrarPickerInicio by remember { mutableStateOf(false) }
    var mostrarPickerFin by remember { mutableStateOf(false) }
    var mostrarPickerInicioAct by remember { mutableStateOf(false) }
    var mostrarPickerFinAct by remember { mutableStateOf(false) }

    val stateInicio = rememberDatePickerState()
    val stateFin = rememberDatePickerState()
    val stateInicioAct = rememberDatePickerState()
    val stateFinAct = rememberDatePickerState()

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
                FloatingActionButton(onClick = { mostrarModalNuevaActividad = true }) { Icon(Icons.Default.Add, null) }
            }
        }
    ) { padding ->
        Box(modifier = Modifier.padding(padding)) {
            when (subPestanaSeleccionada) {
                0 -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(24.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            OutlinedTextField(
                                value = nombreCurso,
                                onValueChange = { nombreCurso = it },
                                label = { Text("Nombre") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            OutlinedTextField(
                                value = descripcion,
                                onValueChange = { descripcion = it },
                                label = { Text("Descripción") },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }
                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = fechaInicio,
                                    onValueChange = {},
                                    label = { Text("Fecha Inicio") },
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth()
                                ); Box(
                                modifier = Modifier.matchParentSize()
                                    .clickable { mostrarPickerInicio = true })
                            }
                        }
                        item {
                            Box(modifier = Modifier.fillMaxWidth()) {
                                OutlinedTextField(
                                    value = fechaFin,
                                    onValueChange = {},
                                    label = { Text("Fecha Fin") },
                                    readOnly = true,
                                    modifier = Modifier.fillMaxWidth()
                                ); Box(
                                modifier = Modifier.matchParentSize()
                                    .clickable { mostrarPickerFin = true })
                            }
                        }
                        item {
                            Button(onClick = {
                                viewModel.editarCursoExistente(
                                    cursoId,
                                    maestroId,
                                    CursoCreate(
                                        nombreCurso,
                                        maestroId,
                                        descripcion,
                                        fechaInicio,
                                        fechaFin,
                                        cursoActual.color_banner ?: "#3F51B5"
                                    )
                                ) { onEdicionCompleta() }
                            }, modifier = Modifier.fillMaxWidth()) { Text("Guardar Cambios") }
                        }
                    }
                }

                1 -> {
                    if (viewModel.listaAlumnosInscritosCurso.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) { Text("No hay alumnos inscritos.") }
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            items(viewModel.listaAlumnosInscritosCurso) { alumno ->
                                Card(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                                    ListItem(
                                        headlineContent = { Text("${alumno.nombre} ${alumno.apellido_paterno}") },
                                        leadingContent = { Icon(Icons.Default.Person, null) }
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    // Si viewModel.listaActividadesCurso cambia, esto se redibujará
                    val actividades = viewModel.listaActividadesCurso

                    if (actividades.isEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No hay actividades registradas.")
                        }
                    } else {
                        LazyColumn(contentPadding = PaddingValues(16.dp)) {
                            // AÑADIR 'key' ES VITAL PARA LA REACTIVIDAD
                            items(items = actividades, key = { it.id }) { act ->
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
    }

    // Modal Crear Actividad
    if (mostrarModalNuevaActividad) {
        AlertDialog(onDismissRequest = { mostrarModalNuevaActividad = false }, title = { Text("Asignar Actividad") }, text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(value = tituloActividad, onValueChange = { tituloActividad = it }, label = { Text("Título") }, modifier = Modifier.fillMaxWidth())
                Box(modifier = Modifier.fillMaxWidth()) { OutlinedTextField(value = fechaInicioAct, onValueChange = {}, label = { Text("Inicio") }, readOnly = true, modifier = Modifier.fillMaxWidth(), trailingIcon = { Icon(Icons.Default.DateRange, null) }); Box(modifier = Modifier.matchParentSize().clickable { mostrarPickerInicioAct = true }) }
                Box(modifier = Modifier.fillMaxWidth()) { OutlinedTextField(value = fechaFinAct, onValueChange = {}, label = { Text("Límite") }, readOnly = true, modifier = Modifier.fillMaxWidth(), trailingIcon = { Icon(Icons.Default.DateRange, null) }); Box(modifier = Modifier.matchParentSize().clickable { mostrarPickerFinAct = true }) }
            }
        }, confirmButton = { Button(onClick = { viewModel.crearActividadParaMateria(cursoId, tituloActividad, descActividad, puntosActividad.toFloatOrNull() ?: 100f, fechaInicioAct, fechaFinAct) { mostrarModalNuevaActividad = false } }) { Text("Publicar") } })
    }

    // Pickers
    if (mostrarPickerInicio) { DatePickerDialog(onDismissRequest = { mostrarPickerInicio = false }, confirmButton = { TextButton(onClick = { stateInicio.selectedDateMillis?.let { fechaInicio = dateFormat.format(Date(it)) }; mostrarPickerInicio = false }) { Text("OK") } }) { DatePicker(state = stateInicio) } }
    if (mostrarPickerFin) { DatePickerDialog(onDismissRequest = { mostrarPickerFin = false }, confirmButton = { TextButton(onClick = { stateFin.selectedDateMillis?.let { fechaFin = dateFormat.format(Date(it)) }; mostrarPickerFin = false }) { Text("OK") } }) { DatePicker(state = stateFin) } }
    if (mostrarPickerInicioAct) { DatePickerDialog(onDismissRequest = { mostrarPickerInicioAct = false }, confirmButton = { TextButton(onClick = { stateInicioAct.selectedDateMillis?.let { fechaInicioAct = dateFormat.format(Date(it)) }; mostrarPickerInicioAct = false }) { Text("OK") } }) { DatePicker(state = stateInicioAct) } }
    if (mostrarPickerFinAct) { DatePickerDialog(onDismissRequest = { mostrarPickerFinAct = false }, confirmButton = { TextButton(onClick = { stateFinAct.selectedDateMillis?.let { fechaFinAct = dateFormat.format(Date(it)) }; mostrarPickerFinAct = false }) { Text("OK") } }) { DatePicker(state = stateFinAct) } }
}