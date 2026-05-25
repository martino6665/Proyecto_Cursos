package com.example.appcursos.Proyecto.viewmodel

import android.util.Log // Importación obligatoria para usar logs en Android
import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcursos.Proyecto.data.AlumnoCreate
import com.example.appcursos.Proyecto.data.ProfesorCreate
import com.example.appcursos.Proyecto.network.RetrofitCursos
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
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
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
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
        return fechaInput.trim()
    }

    /**
     * Valida de forma matemática si el usuario tiene menos de 13 años respecto a la fecha actual.
     */
    private fun esMenorDe13Anos(fechaInput: String): Boolean {
        val formatosEntrada = arrayOf(
            SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()),
            SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()),
            SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        )

        var fechaNacimiento: Date? = null

        // Intentamos parsear la fecha ingresada para la validación matemática
        for (formato in formatosEntrada) {
            try {
                fechaNacimiento = formato.parse(fechaInput.trim())
                if (fechaNacimiento != null) break
            } catch (e: Exception) {
                // Continúa
            }
        }

        if (fechaNacimiento == null) return false // Si no se puede parsear, permitimos que el servidor valide el String

        // Calculamos la edad usando el calendario oficial del sistema
        val hoy = Calendar.getInstance()
        val nacimiento = Calendar.getInstance().apply { time = fechaNacimiento }

        var edad = hoy.get(Calendar.YEAR) - nacimiento.get(Calendar.YEAR)

        // Ajuste fino por si no ha pasado su cumpleaños en el año actual
        if (hoy.get(Calendar.DAY_OF_YEAR) < nacimiento.get(Calendar.DAY_OF_YEAR)) {
            edad--
        }

        return edad < 13
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

        // --- ADUANA DE CONTROL CRÍTICA: VALIDACIÓN DE EDAD ---
        if (esMenorDe13Anos(fecha)) {
            mensajeError = "error, poner una edad adecuada"
            return
        }

        viewModelScope.launch {
            cargando = true
            mensajeError = ""
            try {
                // Sincronización de formato con la base de datos
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
                mensajeError = "Falla al procesar el registro. Verifica el formato de tus datos."
                Log.e("error_Api", e.message.toString())
            } finally {
                cargando = false
            }
        }
    }
}