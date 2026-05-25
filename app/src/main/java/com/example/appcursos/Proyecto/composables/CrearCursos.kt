package com.example.appcursos.Proyecto.view

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appcursos.Proyecto.data.CursoCreate
import com.example.appcursos.Proyecto.viewmodel.ProfesorViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CrearCursoScreen(
    maestroId: Int,
    viewModel: ProfesorViewModel,
    onCursoCreado: () -> Unit
) {
    var nombreCurso by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fechaInicio by remember { mutableStateOf("") }
    var fechaFin by remember { mutableStateOf("") }

    val paletaColores = listOf("#3F51B5", "#009688", "#FF9800", "#E91E63", "#673AB7")
    var colorSeleccionado by remember { mutableStateOf(paletaColores[0]) }

    var mostrarPickerInicio by remember { mutableStateOf(false) }
    var mostrarPickerFin by remember { mutableStateOf(false) }
    val stateInicio = rememberDatePickerState()
    val stateFin = rememberDatePickerState()

    val isFormValid = nombreCurso.isNotBlank() && fechaInicio.isNotBlank() && fechaFin.isNotBlank() && (fechaInicio <= fechaFin)

    Scaffold(
        topBar = { TopAppBar(title = { Text("Crear Nuevo Curso", fontWeight = FontWeight.Bold) }) }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.padding(paddingValues).fillMaxSize().padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item { OutlinedTextField(value = nombreCurso, onValueChange = { nombreCurso = it }, label = { Text("Nombre del Curso") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), enabled = !viewModel.cargando) }
            item { OutlinedTextField(value = descripcion, onValueChange = { descripcion = it }, label = { Text("Descripción") }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), maxLines = 3, enabled = !viewModel.cargando) }

            item { Box(modifier = Modifier.fillMaxWidth()) { OutlinedTextField(value = fechaInicio, onValueChange = {}, label = { Text("Fecha de Inicio") }, leadingIcon = { Icon(Icons.Default.DateRange, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), readOnly = true); if (!viewModel.cargando) Box(modifier = Modifier.matchParentSize().clickable { mostrarPickerInicio = true }) } }
            item { Box(modifier = Modifier.fillMaxWidth()) { OutlinedTextField(value = fechaFin, onValueChange = {}, label = { Text("Fecha de Finalización") }, leadingIcon = { Icon(Icons.Default.DateRange, null) }, modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), readOnly = true); if (!viewModel.cargando) Box(modifier = Modifier.matchParentSize().clickable { mostrarPickerFin = true }) } }

            item {
                Column(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    Text("Selecciona el color:", style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        paletaColores.forEach { hex ->
                            Box(modifier = Modifier.size(40.dp).clip(CircleShape).background(Color(android.graphics.Color.parseColor(hex))).then(if (colorSeleccionado == hex) Modifier.border(3.dp, MaterialTheme.colorScheme.onBackground, CircleShape) else Modifier).clickable(enabled = !viewModel.cargando) { colorSeleccionado = hex })
                        }
                    }
                }
            }

            item {
                if (viewModel.mensajeStatus.isNotEmpty()) Text(text = viewModel.mensajeStatus, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)

                Button(
                    onClick = {
                        val nuevoCurso = CursoCreate(nombreCurso.trim(), maestroId, descripcion.trim(), fechaInicio, fechaFin, colorSeleccionado)
                        // MEJORA: Pasamos maestroId al crearCurso para que el ViewModel refresque la lista automáticamente
                        viewModel.crearCurso(nuevoCurso, maestroId) {
                            onCursoCreado()
                        }
                    },
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    enabled = !viewModel.cargando && isFormValid
                ) {
                    if (viewModel.cargando) CircularProgressIndicator(color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier.size(24.dp))
                    else Text("Guardar Curso", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }

    if (mostrarPickerInicio) { DatePickerDialog(onDismissRequest = { mostrarPickerInicio = false }, confirmButton = { TextButton(onClick = { stateInicio.selectedDateMillis?.let { fechaInicio = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }.format(Date(it)) }; mostrarPickerInicio = false }) { Text("Aceptar") } }) { DatePicker(state = stateInicio) } }
    if (mostrarPickerFin) { DatePickerDialog(onDismissRequest = { mostrarPickerFin = false }, confirmButton = { TextButton(onClick = { stateFin.selectedDateMillis?.let { fechaFin = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).apply { timeZone = TimeZone.getTimeZone("UTC") }.format(Date(it)) }; mostrarPickerFin = false }) { Text("OK") } }) { DatePicker(state = stateFin) } }
}