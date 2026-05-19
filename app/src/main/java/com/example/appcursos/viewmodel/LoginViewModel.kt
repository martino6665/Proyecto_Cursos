package com.example.appcursos.Proyecto.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcursos.Proyecto.data.LoginRequest
import com.example.appcursos.Proyecto.network.RetrofitCursos
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {

    // Estados para controlar la interfaz de usuario
    var cargando by mutableStateOf(false)
    var mensajeError by mutableStateOf("")

    /**
     * Intenta iniciar sesión en VisionEducation.
     * @param usuario El nombre de usuario ingresado en el campo de texto.
     * @param pass La contraseña ingresada.
     * @param onSuccess Callback que devuelve el ROL ("alumno" o "profesor").
     */
    fun login(usuario: String, pass: String, onSuccess: (String) -> Unit) {

        // --- 1. VALIDACIÓN DE ENTRADA ---
        val userLimpio = usuario.trim()
        val passLimpia = pass.trim()

        if (userLimpio.isBlank() || passLimpia.isBlank()) {
            mensajeError = "Por favor, ingresa tu usuario y contraseña"
            return
        }

        viewModelScope.launch {
            cargando = true
            mensajeError = ""
            try {
                // --- 2. PETICIÓN A LA API ---
                // Aquí usamos 'nombre_usuario' para que coincida con el backend en Render
                val request = LoginRequest(
                    nombre_usuario = userLimpio,
                    password = passLimpia
                )

                val response = RetrofitCursos.apiCursosService.iniciarSesion(request)

                // --- 3. PROCESAMIENTO DE RESPUESTA ---
                if (response.estado == "Exitoso") {

                    onSuccess(response.rol ?: "alumno")
                } else {

                    mensajeError = response.mensaje
                }
            } catch (e: Exception) {

                mensajeError = "Error de conexión: No se pudo contactar con el servidor"
            } finally {
                cargando = false
            }
        }
    }
}