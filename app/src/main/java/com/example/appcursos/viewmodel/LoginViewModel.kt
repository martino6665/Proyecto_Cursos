package com.example.appcursos.Proyecto.viewmodel

import androidx.compose.runtime.*
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appcursos.Proyecto.data.LoginRequest
import com.example.appcursos.Proyecto.network.RetrofitCursos
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel : ViewModel() {

    var cargando by mutableStateOf(false)
    var mensajeError by mutableStateOf("")

    /**
     * Intenta iniciar sesión regresando el ROL y el ID REAL del usuario.
     */
    fun login(usuario: String, pass: String, onSuccess: (String, Int) -> Unit) {

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
                val request = LoginRequest(
                    nombre_usuario = userLimpio,
                    password = passLimpia
                )

                val response = RetrofitCursos.apiCursosService.iniciarSesion(request)

                if (response.estado == "Exitoso") {
                    // COINCIDENCIA ABSOLUTA: Lee la variable exacta del CursoModel modificado
                    val idReal = response.usuario_id ?: 0
                    val rolReal = response.rol ?: "alumno"

                    onSuccess(rolReal, idReal)
                } else {
                    mensajeError = response.mensaje
                }
            } catch (e: HttpException) {
                mensajeError = "Credenciales inválidas o datos incorrectos"
            } catch (e: Exception) {
                mensajeError = "Error de conexión: El servidor está despertando, intenta de nuevo"
            } finally {
                cargando = false
            }
        }
    }
}