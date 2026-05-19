package com.example.appcursos.Proyecto.data

import com.google.gson.annotations.SerializedName

// ==========================================
// --- MOLDES PARA REGISTROS (ALUMNOS Y PROFESORES) ---
// ==========================================

data class AlumnoCreate(
    @SerializedName("nombre_usuario") val nombre_usuario: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido_paterno") val apellido_paterno: String,
    @SerializedName("apellido_materno") val apellido_materno: String,
    @SerializedName("fecha_nacimiento") val fecha_nacimiento: String,
    @SerializedName("password") val password: String
)

data class AlumnoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_usuario") val nombre_usuario: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido_paterno") val apellido_paterno: String,
    @SerializedName("apellido_materno") val apellido_materno: String,
    @SerializedName("fecha_nacimiento") val fecha_nacimiento: String,
    @SerializedName("rol") val rol: String
)

data class ProfesorCreate(
    @SerializedName("nombre_usuario") val nombre_usuario: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido_paterno") val apellido_paterno: String,
    @SerializedName("apellido_materno") val apellido_materno: String,
    @SerializedName("fecha_nacimiento") val fecha_nacimiento: String,
    @SerializedName("password") val password: String
)

data class ProfesorResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_usuario") val nombre_usuario: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido_paterno") val apellido_paterno: String,
    @SerializedName("apellido_materno") val apellido_materno: String,
    @SerializedName("fecha_nacimiento") val fecha_nacimiento: String,
    @SerializedName("rol") val rol: String
)

// ==========================================
// --- MOLDES PARA GESTIÓN DE CURSOS Y ACCIONES ---
// ==========================================

data class CursoCreate(
    @SerializedName("nombre_del_curso") val nombre_del_curso: String,
    @SerializedName("id_del_profesor") val id_del_profesor: Int,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha_de_inicio") val fecha_de_inicio: String,
    @SerializedName("fecha_de_fin") val fecha_de_fin: String
)

data class CursoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_del_curso") val nombre_del_curso: String,
    @SerializedName("id_del_profesor") val id_del_profesor: Int,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha_de_inicio") val fecha_de_inicio: String,
    @SerializedName("fecha_de_fin") val fecha_de_fin: String
)

data class CalificacionUpdate(
    @SerializedName("nota") val nota: Int
)

data class InscripcionRequest(
    @SerializedName("alumno_id") val alumno_id: Int,
    @SerializedName("curso_id") val curso_id: Int
)

data class InscripcionResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("alumno_id") val alumno_id: Int,
    @SerializedName("curso_id") val curso_id: Int,
    @SerializedName("calificacion") val calificacion: Int? = null
)

data class SimpleResponse(
    @SerializedName("estado") val estado: String,
    @SerializedName("mensaje") val mensaje: String
)

// ==========================================
// --- MOLDES ADICIONALES PARA CONSULTAS COMPUESTAS ---
// ==========================================

data class AlumnoCursoDetalleResponse(
    @SerializedName("inscripcion_id") val inscripcionId: Int,
    @SerializedName("curso_id") val cursoId: Int,
    @SerializedName("nombre_del_curso") val nombreDelCurso: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("calificacion") val calificacion: Int?,
    @SerializedName("nombre_profesor") val nombreProfesor: String
)

data class AlumnoInscritoResponse(
    @SerializedName("alumno_id") val alumnoId: Int,
    @SerializedName("nombre_usuario") val nombreUsuario: String,
    @SerializedName("nombre_completo") val nombreCompleto: String,
    @SerializedName("calificacion") val calificacion: Int?
)

data class LoginResponse(
    @SerializedName("estado") val estado: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("rol") val rol: String? = null
)

data class LoginRequest(
    @SerializedName("nombre_usuario") val nombre_usuario: String,
    @SerializedName("password") val password: String
)