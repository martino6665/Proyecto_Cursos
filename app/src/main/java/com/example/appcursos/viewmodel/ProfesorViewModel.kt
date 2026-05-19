package com.example.appcursos.Proyecto.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcursos.Proyecto.data.*
import com.example.appcursos.Proyecto.network.RetrofitCursos
import kotlinx.coroutines.launch

class ProfesorViewModel : ViewModel() {

    var listaCursos by mutableStateOf<List<CursoResponse>>(emptyList())
    var cargando by mutableStateOf(false)
    var mensajeStatus by mutableStateOf("")

    // 1. OBTENER CURSOS (Coincide con @app.get("/profesores/{maestro_id}/mis-cursos"))
    fun cargarCursos(maestroId: Int) {
        viewModelScope.launch {
            cargando = true
            mensajeStatus = ""
            try {
                // Llama exactamente a ver_materias_asignadas de tu ApiCursos
                val response = RetrofitCursos.apiCursosService.ver_materias_asignadas(maestroId)
                listaCursos = response
            } catch (e: Exception) {
                mensajeStatus = "Error al cargar cursos"
            } finally {
                cargando = false
            }
        }
    }

    // 2. ELIMINAR CURSO (Coincide con @app.delete("/profesores/cursos/eliminar/{curso_id}/{maestro_id}"))
    fun borrarCurso(cursoId: Int, maestroId: Int) {
        viewModelScope.launch {
            try {
                // Llama exactamente a eliminar_materia de tu ApiCursos
                RetrofitCursos.apiCursosService.eliminar_materia(cursoId, maestroId)
                // Refrescamos la lista automáticamente después de borrar en línea recta
                cargarCursos(maestroId)
                mensajeStatus = "Curso eliminado"
            } catch (e: Exception) {
                mensajeStatus = "No se pudo eliminar el curso"
            }
        }
    }

    // 3. PRIVILEGIO: CREAR CURSO (Coincide con @app.post("/profesores/cursos/crear"))
    fun crearCurso(curso: CursoCreate, onSuccess: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            try {
                // Llama exactamente a crear_materia de tu ApiCursos
                RetrofitCursos.apiCursosService.crear_materia(curso)
                mensajeStatus = "Curso creado exitosamente"
                onSuccess() // Ejecuta la navegación de regreso a la lista
            } catch (e: Exception) {
                mensajeStatus = "Error al crear el curso"
            } finally {
                cargando = false
            }
        }
    }

    // 4. PRIVILEGIO: ASIGNAR/MODIFICAR CALIFICACIÓN (Coincide con @app.put("/profesores/calificar/{alumno_id}/{curso_id}"))
    fun ponerCalificacion(alumnoId: Int, cursoId: Int, nota: Int) {
        viewModelScope.launch {
            try {
                val calificacionUpdate = CalificacionUpdate(nota = nota)
                // Llama exactamente a calificar_alumno de tu ApiCursos
                RetrofitCursos.apiCursosService.calificar_alumno(alumnoId, cursoId, calificacionUpdate)
                mensajeStatus = "Calificación asignada con éxito"
            } catch (e: Exception) {
                mensajeStatus = "Error al asignar la calificación"
            }
        }
    }

    // 5. PRIVILEGIO: UNIRSE A UN CURSO (Coincide con @app.post("/alumnos/inscribir"))
    fun unirseACurso(maestroId: Int, cursoId: Int) {
        viewModelScope.launch {
            try {
                val request = InscripcionRequest(alumno_id = maestroId, curso_id = cursoId)
                // El maestro se inscribe usando el endpoint común de inscripciones
                RetrofitCursos.apiCursosService.inscribirse(request)
                mensajeStatus = "Te has unido al curso correctamente"
            } catch (e: Exception) {
                mensajeStatus = "Error: No te pudiste unir al curso"
            }
        }
    }
}