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

    // Función para obtener los cursos desde Render
    fun cargarCursos(maestroId: Int) {
        viewModelScope.launch {
            cargando = true
            try {
                val response = RetrofitCursos.apiCursosService.obtenerMisCursos(maestroId)
                listaCursos = response
            } catch (e: Exception) {
                mensajeStatus = "Error al cargar cursos"
            } finally {
                cargando = false
            }
        }
    }

    // Función para eliminar un curso
    fun borrarCurso(cursoId: Int, maestroId: Int) {
        viewModelScope.launch {
            try {
                RetrofitCursos.apiCursosService.eliminarCurso(cursoId, maestroId)
                // Refrescamos la lista automáticamente después de borrar
                cargarCursos(maestroId)
                mensajeStatus = "Curso eliminado"
            } catch (e: Exception) {
                mensajeStatus = "No se pudo eliminar el curso"
            }
        }
    }
}