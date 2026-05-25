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

    // --- ESTADOS DE UI Y ERRORES ---
    var cargando by mutableStateOf(false)
    var mensajeStatus by mutableStateOf("")
    var errorUI by mutableStateOf<String?>(null)

    // 1. CARGAR MATERIAS
    fun cargarMateriasDelProfesor(maestroId: Int) {
        if (maestroId <= 0) return
        viewModelScope.launch {
            cargando = true
            try {
                listaCursosAsignados = RetrofitCursos.apiCursosService.ver_materias_asignadas(maestroId)
            } catch (e: Exception) {
                Log.e("VisionEducation", "Error al cargar cursos: ${e.message}")
            } finally {
                cargando = false
            }
        }
    }

    // 2. ELIMINAR CURSO
    fun borrarCurso(cursoId: Int, maestroId: Int) {
        viewModelScope.launch {
            cargando = true
            try {
                RetrofitCursos.apiCursosService.eliminar_materia(cursoId, maestroId)
                cargarMateriasDelProfesor(maestroId)
                mensajeStatus = "Curso eliminado"
            } catch (e: Exception) {
                mensajeStatus = "Error al eliminar."
            } finally {
                cargando = false
            }
        }
    }

    // 3. CREAR CURSO
    fun crearCurso(curso: CursoCreate, maestroId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            try {
                RetrofitCursos.apiCursosService.crear_materia(curso)
                cargarMateriasDelProfesor(maestroId)
                onSuccess()
            } catch (e: Exception) {
                mensajeStatus = "Error: ${e.message}"
            } finally {
                cargando = false
            }
        }
    }

    // 4. EDITAR CURSO
    fun editarCursoExistente(cursoId: Int, maestroId: Int, cursoData: CursoCreate, onSuccess: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            try {
                RetrofitCursos.apiCursosService.actualizar_materia(cursoId, maestroId, cursoData)
                cargarMateriasDelProfesor(maestroId)
                onSuccess()
            } catch (e: Exception) {
                mensajeStatus = "Error al actualizar curso."
            } finally {
                cargando = false
            }
        }
    }

    // 5. CARGAR ALUMNOS INSCRITOS
    fun cargarAlumnosInscritosAlCurso(cursoId: Int) {
        if (cursoId == 0) return
        viewModelScope.launch {
            try {
                listaAlumnosInscritosCurso = RetrofitCursos.apiCursosService.ver_alumnos_activos_en_curso(cursoId)
            } catch (e: Exception) {
                listaAlumnosInscritosCurso = emptyList()
            }
        }
    }

    // 6. CARGAR ACTIVIDADES DEL CURSO
    fun cargarActividadesDelCurso(cursoId: Int) {
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

    // 7. CARGAR PENDIENTES
    fun cargarAgendaPendientesMaestro(maestroId: Int) {
        if (maestroId == 0) return
        viewModelScope.launch {
            try {
                listaAgendaPendientesMaestro = RetrofitCursos.apiCursosService.obtenerPendientesRealMaestro(maestroId)
            } catch (e: Exception) {
                Log.e("VisionEducation", "Error cargando pendientes")
            }
        }
    }

    // 8. CALIFICAR ENTREGA
    fun calificarEntregaActividad(entregaId: Int, nota: Float, comentario: String, maestroId: Int, onExito: () -> Unit) {
        viewModelScope.launch {
            try {
                val body = CalificarEntregaRequest(nota_obtenida = nota, comentario_profesor = comentario)
                RetrofitCursos.apiCursosService.evaluar_entrega_alumno(entregaId, body)
                cargarAgendaPendientesMaestro(maestroId)
                onExito()
            } catch (e: Exception) {
                mensajeStatus = "Error al calificar."
            }
        }
    }

    // 9. CREAR ACTIVIDAD (Sincronizado y con manejo de errores de UI)
    fun crearActividad(cursoId: Int, titulo: String, descripcion: String, puntos: Float, inicio: String, limite: String, onExito: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            errorUI = null
            try {
                val body = ActividadCreate(
                    titulo = titulo,
                    descripcion = descripcion,
                    puntos_maximos = puntos,
                    fecha_inicio = inicio,
                    fecha_limite = limite
                )

                RetrofitCursos.apiCursosService.asignar_actividad_a_materia(cursoId, body)

                cargarActividadesDelCurso(cursoId)
                onExito()
            } catch (e: Exception) {
                Log.e("DEBUG_PUBLISH", "Error: ${e.message}")
                errorUI = "Error 500: Fallo al guardar en servidor."
            } finally {
                cargando = false
            }
        }
    }

    // 10. CARGAR LISTADO GLOBAL
    fun cargarTodosLosAlumnos() {
        viewModelScope.launch {
            cargando = true
            try {
                listaGlobalAlumnos = RetrofitCursos.apiCursosService.obtenerListaDeAlumnos()
            } catch (e: Exception) {
                Log.e("VisionEducation", "Error al cargar alumnos")
            } finally {
                cargando = false
            }
        }
    }
}