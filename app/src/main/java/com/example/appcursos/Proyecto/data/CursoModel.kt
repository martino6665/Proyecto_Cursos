package com.example.appcursos.Proyecto.data

// Esto coincide con 'CursoCreate' en tu dtos.py
data class CursoCreate(
    val nombre_del_curso: String,
    val id_del_profesor: Int,
    val descripcion: String,
    val fecha_de_inicio: String, // Formato "2026-05-20"
    val fecha_de_fin: String
)

// Esto coincide con 'CursoResponse' en tu dtos.py
data class CursoResponse(
    val id: Int,
    val nombre_del_curso: String,
    val id_del_profesor: Int,
    val descripcion: String,
    val fecha_de_inicio: String,
    val fecha_de_fin: String
)

// Clase para las respuestas de eliminación o acciones simples
data class SimpleResponse(
    val mensaje: String
)