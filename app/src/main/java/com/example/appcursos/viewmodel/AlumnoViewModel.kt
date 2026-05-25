package com.example.appcursos.Proyecto.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcursos.Proyecto.data.*
import com.example.appcursos.Proyecto.network.RetrofitCursos
import kotlinx.coroutines.launch
import android.util.Log

class AlumnoViewModel : ViewModel() {

    // Listas de datos
    var listaCursosInscritos by mutableStateOf<List<CursoResponse>>(emptyList()); private set
    var listaCursosDisponibles by mutableStateOf<List<CursoResponse>>(emptyList()); private set
    var listaAgendaEstudiante by mutableStateOf<List<EntregaResponse>>(emptyList()); private set
    var listaActividadesAlumno by mutableStateOf<List<ActividadResponse>>(emptyList()); private set

    // Estados de control de UI
    var cargando by mutableStateOf(false)
    var mensajeStatus by mutableStateOf("")

    // 1. CARGAR CURSOS DEL ALUMNO
    fun cargarCursosDelAlumno(alumnoId: Int) {
        viewModelScope.launch {
            cargando = true
            try {
                listaCursosInscritos = RetrofitCursos.apiCursosService.ver_cursos_inscritos(alumnoId)
            } catch (e: Exception) {
                Log.e("VisionEducation", "Error al cargar cursos: ${e.message}")
            } finally {
                cargando = false
            }
        }
    }

    // 2. CARGAR TODOS LOS CURSOS DISPONIBLES
    fun cargarTodosLosCursosDisponibles() {
        viewModelScope.launch {
            cargando = true
            try {
                listaCursosDisponibles = RetrofitCursos.apiCursosService.buscar_todos_los_cursos("")
            } catch (e: Exception) {
                Log.e("VisionEducation", "Error al cargar catálogo: ${e.message}")
            } finally {
                cargando = false
            }
        }
    }

    // 3. INSCRIBIRSE A UN CURSO
    fun inscribirseACurso(alumnoId: Int, cursoId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            try {
                RetrofitCursos.apiCursosService.inscribirse(InscripcionCreate(alumnoId, cursoId))
                mensajeStatus = "¡Inscripción exitosa!"
                cargarCursosDelAlumno(alumnoId)
                onSuccess()
            } catch (e: Exception) {
                mensajeStatus = "Error al inscribirse."
            } finally {
                cargando = false
            }
        }
    }

    // 4. CARGAR AGENDA ESCOLAR
    fun cargarAgendaEscolarEstudiante(alumnoId: Int) {
        viewModelScope.launch {
            cargando = true
            try {
                listaAgendaEstudiante = RetrofitCursos.apiCursosService.obtenerAgendaRealAlumno(alumnoId)
            } catch (e: Exception) {
                Log.e("VisionEducation", "Error agenda: ${e.message}")
            } finally {
                cargando = false
            }
        }
    }

    // 5. CARGAR ACTIVIDADES DE UN CURSO (MEJORA: Refresco de actividades)
    fun cargarActividadesParaAlumno(cursoId: Int) {
        viewModelScope.launch {
            cargando = true
            try {
                listaActividadesAlumno = RetrofitCursos.apiCursosService.obtenerActividadesPorCurso(cursoId)
            } catch (e: Exception) {
                Log.e("VisionEducation", "Error cargar actividades: ${e.message}")
                listaActividadesAlumno = emptyList()
            } finally {
                cargando = false
            }
        }
    }

    // 6. ENVÍO DE TAREA (MEJORA: Refresco automático tras entrega)
    fun enviarActividadResuelta(actividadId: Int, alumnoId: Int, cursoId: Int, contenido: String, onEntregaExitosa: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            try {
                RetrofitCursos.apiCursosService.entregar_actividad(actividadId, alumnoId, EntregaCreate(contenido))
                mensajeStatus = "¡Tarea enviada correctamente!"

                // Refrescamos ambos: la agenda (donde aparece lo enviado) y las actividades del curso
                cargarAgendaEscolarEstudiante(alumnoId)
                cargarActividadesParaAlumno(cursoId)

                onEntregaExitosa()
            } catch (e: Exception) {
                Log.e("VisionEducation", "Error al subir tarea: ${e.message}")
                mensajeStatus = "Error al enviar tarea."
            } finally {
                cargando = false
            }
        }
    }
}