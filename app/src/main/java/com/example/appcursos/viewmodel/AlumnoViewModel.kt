package com.example.appcursos.Proyecto.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcursos.Proyecto.data.CursoResponse
import com.example.appcursos.Proyecto.data.InscripcionRequest
import com.example.appcursos.Proyecto.network.RetrofitCursos
import kotlinx.coroutines.launch

class AlumnoViewModel : ViewModel() {

    var listaCursosInscritos by mutableStateOf<List<CursoResponse>>(emptyList())
    var cargando by mutableStateOf(false)
    var mensajeStatus by mutableStateOf("")

    // 1. VER CURSOS INSCRITOS (Coincide con @app.get("/alumnos/{alumno_id}/mis-cursos"))
    fun cargarCursosDelAlumno(alumnoId: Int) {
        viewModelScope.launch {
            cargando = true
            mensajeStatus = ""
            try {
                // Golpeamos el endpoint exclusivo del alumno en espejo con Python
                val response = RetrofitCursos.apiCursosService.ver_cursos_inscritos(alumnoId)
                listaCursosInscritos = response
            } catch (e: Exception) {
                mensajeStatus = "Error al obtener tus cursos desde VisionEducation"
            } finally {
                cargando = false
            }
        }
    }

    // 2. INSCRIBIRSE A UN CURSO NUEVO (Coincide con @app.post("/alumnos/inscribir"))
    fun inscribirseACurso(alumnoId: Int, cursoId: Int, onSuccess: () -> Unit) {
        viewModelScope.launch {
            cargando = true
            mensajeStatus = ""
            try {
                val request = InscripcionRequest(alumno_id = alumnoId, curso_id = cursoId)
                RetrofitCursos.apiCursosService.inscribirse(request)
                mensajeStatus = "¡Inscripción exitosa!"

                // Refrescamos automáticamente la lista para que aparezca el nuevo curso en pantalla
                cargarCursosDelAlumno(alumnoId)
                onSuccess()
            } catch (e: Exception) {
                mensajeStatus = "No se pudo realizar la inscripción"
            } finally {
                cargando = false
            }
        }
    }
}