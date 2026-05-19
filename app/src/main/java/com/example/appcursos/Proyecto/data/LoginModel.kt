package com.example.appcursos.Proyecto.data

// Este es el molde para enviar datos al servidor
data class LoginRequest(
    val nombre_usuario: String, // CAMBIADO: Antes decía 'usuario', ahora coincide con la DB
    val password: String
)

// Este es el molde para recibir la respuesta del servidor
data class LoginResponse(
    val estado: String,   // Recibirá "Exitoso" o "Error"
    val mensaje: String,  // Recibirá el "Bienvenido..." o el mensaje de error
    val rol: String? = null // Recibirá "alumno" o "profesor"
)