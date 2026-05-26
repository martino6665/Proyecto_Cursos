package com.example.appcursos.Proyecto.viewmodel

import android.util.Log
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcursos.Proyecto.data.*
import com.example.appcursos.Proyecto.network.RetrofitCursos
import kotlinx.coroutines.launch

class ProfesorViewModel : ViewModel() {

    // --- ESTADOS DE DATOS ---
    var listaCursosAsignados by mutableStateOf<List<CursoResponse>>(emptyList()); private set
    var listaAlumnosInscritosCurso by mutableStateOf<List<UsuarioResponse>>(emptyList()); private set
    var listaAgendaPendientesMaestro by mutableStateOf<List<EntregaResponse>>(emptyList()); private set
    var listaGlobalAlumnos by mutableStateOf<List<AlumnoResponse>>(emptyList()); private set
    var listaActividadesCurso by mutableStateOf<List<ActividadResponse>>(emptyList()); private set

    // --- ESTADOS DE UI ---
    var cargando by mutableStateOf(false)
    var mensajeStatus by mutableStateOf("")
    var errorUI by mutableStateOf<String?>(null)

    // --- 1. GESTIÓN DE CURSOS ---
    fun cargarMateriasDelProfesor(maestroId: Int) {
        if (maestroId <= 0) return
        viewModelScope.launch {
            cargando = true
            try {
                // Al asignar el resultado directamente, Jetpack Compose detecta el cambio
                val resultado = RetrofitCursos.apiCursosService.ver_materias_asignadas(maestroId)
                listaCursosAsignados = resultado
                Log.d("DEBUG_API", "Cursos recibidos: ${resultado.size}")
            } catch (e: Exception) {
                errorUI = "Error: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }
    fun crearCurso(curso: CursoCreate, maestroId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            try {
                RetrofitCursos.apiCursosService.crear_materia(curso)
                // RECARGA INMEDIATA: Al crear, actualizamos la lista
                cargarMateriasDelProfesor(maestroId)
                mensajeStatus = "Curso creado con éxito"
                onSuccess()
            } catch (e: Exception) {
                errorUI = "Error al crear curso"
            } finally {
                cargando = false
            }
        }
    }

    fun borrarCurso(cursoId: Int, maestroId: Int) {
        viewModelScope.launch {
            cargando = true
            try {
                RetrofitCursos.apiCursosService.eliminar_materia(cursoId, maestroId)
                cargarMateriasDelProfesor(maestroId)
                mensajeStatus = "Curso eliminado"
            } catch (e: Exception) {
                errorUI = "Error al eliminar curso"
            } finally {
                cargando = false
            }
        }
    }

    fun editarCursoExistente(cursoId: Int, maestroId: Int, cursoData: CursoCreate, onSuccess: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            try {
                RetrofitCursos.apiCursosService.actualizar_materia(cursoId, maestroId, cursoData)
                cargarMateriasDelProfesor(maestroId)
                onSuccess()
            } catch (e: Exception) {
                errorUI = "Error al actualizar curso"
            } finally {
                cargando = false
            }
        }
    }

    // --- 2. ACTIVIDADES Y ALUMNOS ---
    fun cargarActividadesDelCurso(cursoId: Int) {
        if (cursoId <= 0) return
        viewModelScope.launch {
            cargando = true
            try {
                listaActividadesCurso = RetrofitCursos.apiCursosService.obtenerActividadesPorCurso(cursoId)
            } catch (e: Exception) {
                listaActividadesCurso = emptyList()
            } finally {
                cargando = false
            }
        }
    }

    fun crearActividad(cursoId: Int, titulo: String, descripcion: String, puntos: Float, inicio: String, limite: String, onExito: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            try {
                val body = ActividadCreate(titulo, descripcion, puntos, inicio, limite)
                RetrofitCursos.apiCursosService.asignar_actividad_a_materia(cursoId, body)
                cargarActividadesDelCurso(cursoId)
                onExito()
            } catch (e: Exception) {
                errorUI = "Error al guardar actividad"
            } finally {
                cargando = false
            }
        }
    }

    fun cargarAlumnosInscritosAlCurso(cursoId: Int) {
        if (cursoId <= 0) return
        viewModelScope.launch {
            try {
                listaAlumnosInscritosCurso = RetrofitCursos.apiCursosService.ver_alumnos_activos_en_curso(cursoId)
            } catch (e: Exception) {
                listaAlumnosInscritosCurso = emptyList()
            }
        }
    }

    // --- 3. PENDIENTES Y CALIFICACIÓN ---
    fun cargarAgendaPendientesMaestro(maestroId: Int) {
        if (maestroId <= 0) return
        viewModelScope.launch {
            try {
                listaAgendaPendientesMaestro = RetrofitCursos.apiCursosService.obtenerPendientesRealMaestro(maestroId)
            } catch (e: Exception) {
                Log.e("ProfesorViewModel", "Error cargando pendientes")
            }
        }
    }

    fun calificarEntregaActividad(entregaId: Int, nota: Float, comentario: String, maestroId: Int, onExito: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            try {
                val body = CalificarEntregaRequest(nota, comentario)
                RetrofitCursos.apiCursosService.evaluar_entrega_alumno(entregaId, body)
                cargarAgendaPendientesMaestro(maestroId)
                onExito()
            } catch (e: Exception) {
                errorUI = "Error al calificar"
            } finally {
                cargando = false
            }
        }
    }

    fun cargarTodosLosAlumnos() {
        viewModelScope.launch {
            cargando = true
            try {
                listaGlobalAlumnos = RetrofitCursos.apiCursosService.obtenerListaDeAlumnos()
            } catch (e: Exception) {
                errorUI = "Error al cargar alumnos"
            } finally {
                cargando = false
            }
        }
    }
}