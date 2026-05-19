package com.example.appcursos.Proyecto.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcursos.Proyecto.data.AlumnoCreate
import com.example.appcursos.Proyecto.data.ProfesorCreate
import com.example.appcursos.Proyecto.network.RetrofitCursos
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class RegistroViewModel : ViewModel() {

    var cargando by mutableStateOf(false)
    var mensajeError by mutableStateOf("")

    /**
     * Convierte formatos comunes de fecha (DD/MM/YYYY o DD-MM-YYYY) al estándar estricto de Python (YYYY-MM-DD)
     */
    private fun formatearFechaParaPython(fechaInput: String): String {
        val formatosEntrada = arrayOf(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) // Por si ya viene correcto
        )

        val formatoSalida = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        for (formato in formatosEntrada) {
            try {
                val fechaParseada = formato.parse(fechaInput.trim())
                if (fechaParseada != null) {
                    return formatoSalida.format(fechaParseada)
                }
            } catch (e: Exception) {
                // Sigue probando el siguiente formato si este falla
            }
        }
        return fechaInput.trim() // Si no pudo parsear nada, lo manda tal cual para que valide el servidor
    }

    fun registrarUsuario(
        rol: String,
        usuario: String,
        nombre: String,
        paterno: String,
        materno: String,
        fecha: String,
        pass: String,
        onSuccess: () -> Unit
    ) {
        if (usuario.isBlank() || nombre.isBlank() || pass.isBlank() || fecha.isBlank()) {
            mensajeError = "Por favor, llena todos los campos obligatorios"
            return
        }

        viewModelScope.launch {
            cargando = true
            mensajeError = ""
            try {
                // LOGICA MEJORADA: Formateamos la fecha para hablar el mismo idioma que Python
                val fechaFormateada = formatearFechaParaPython(fecha)

                if (rol == "profesor") {
                    val datosProfesor = ProfesorCreate(
                        nombre_usuario = usuario.trim(),
                        nombre = nombre.trim(),
                        apellido_paterno = paterno.trim(),
                        apellido_materno = materno.trim(),
                        fecha_nacimiento = fechaFormateada,
                        password = pass.trim()
                    )
                    RetrofitCursos.apiCursosService.registrar_profesor(datosProfesor)
                } else {
                    val datosAlumno = AlumnoCreate(
                        nombre_usuario = usuario.trim(),
                        nombre = nombre.trim(),
                        apellido_paterno = paterno.trim(),
                        apellido_materno = materno.trim(),
                        fecha_nacimiento = fechaFormateada,
                        password = pass.trim()
                    )
                    RetrofitCursos.apiCursosService.registrar_alumno(datosAlumno)
                }

                onSuccess()
            } catch (e: Exception) {
                // Si el error fue un 422 u otra cosa, te lo avisará de manera más clara para debuguear
                mensajeError = "Falla al procesar el registro. Verifica el formato de tus datos."
            } finally {
                cargando = false
            }
        }
    }
}