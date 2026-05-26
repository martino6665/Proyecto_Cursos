package com.example.appcursos.Proyecto.data

import com.google.gson.annotations.SerializedName

// ==============================================================================
// --- 👤 MOLDES PARA REGISTROS (ALUMNOS Y PROFESORES) ---
// ==============================================================================

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


// ==============================================================================
// --- 📚 MOLDES PARA GESTIÓN DE CURSOS Y ACCIONES ---
// ==============================================================================

data class CursoCreate(
    @SerializedName("nombre_del_curso") val nombre_del_curso: String,
    @SerializedName("id_del_profesor") val id_del_profesor: Int,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha_de_inicio") val fecha_de_inicio: String,
    @SerializedName("fecha_de_fin") val fecha_de_fin: String,
    @SerializedName("color_banner") val color_banner: String
)

data class CursoResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_del_curso") val nombre_del_curso: String,
    @SerializedName("id_del_profesor") val id_del_profesor: Int,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha_de_inicio") val fecha_de_inicio: String,
    @SerializedName("fecha_de_fin") val fecha_de_fin: String,
    @SerializedName("color_banner") val color_banner: String? = null
)

// --- MEJORA: MODELO PARA VISTA DEL ALUMNO (UI-READY) ---
data class CursoConDetallesResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_del_curso") val nombre_del_curso: String,
    @SerializedName("descripcion") val descripcion: String,
    @SerializedName("fecha_de_inicio") val fecha_de_inicio: String,
    @SerializedName("fecha_de_fin") val fecha_de_fin: String,
    @SerializedName("color_banner") val color_banner: String?,
    @SerializedName("nombre_profesor") val nombre_profesor: String
)

data class CalificacionUpdate(
    @SerializedName("nota") val nota: Int
)

data class InscripcionCreate(
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


// ==============================================================================
// --- 🗂️ MOLDES ADICIONALES PARA CONSULTAS COMPUESTAS ---
// ==============================================================================

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

data class UsuarioDetalleResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_completo") val nombre_completo: String,
    @SerializedName("nombre_usuario") val nombre_usuario: String,
    @SerializedName("rol") val rol: String
)

data class LoginResponse(
    @SerializedName("estado") val estado: String,
    @SerializedName("mensaje") val mensaje: String,
    @SerializedName("rol") val rol: String? = null,
    @SerializedName("usuario_id") val usuario_id: Int? = null
)

data class LoginRequest(
    @SerializedName("nombre_usuario") val nombre_usuario: String,
    @SerializedName("password") val password: String
)


// ==============================================================================
// --- 📝 NUEVOS MOLDES: ACTIVIDADES, ENTREGAS Y EVALUACIONES ---
// ==============================================================================

data class ActividadCreate(
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("puntos_maximos") val puntos_maximos: Float,
    @SerializedName("fecha_inicio") val fecha_inicio: String?,
    @SerializedName("fecha_limite") val fecha_limite: String?
)

data class ActividadResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("curso_id") val curso_id: Int,
    @SerializedName("titulo") val titulo: String,
    @SerializedName("descripcion") val descripcion: String?,
    @SerializedName("puntos_maximos") val puntos_maximos: Float,
    @SerializedName("fecha_inicio") val fecha_inicio: String?,
    @SerializedName("fecha_limite") val fecha_limite: String?
)

// ==============================================================================
// --- 📤 MOLDES PARA ENTREGAS Y CALIFICACIONES ---
// ==============================================================================

data class EntregaCreate(
    @SerializedName("contenido_entrega") val contenido_entrega: String
)

data class EntregaResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("actividad_id") val actividad_id: Int,
    @SerializedName("alumno_id") val alumno_id: Int,
    @SerializedName("contenido_entrega") val contenido_entrega: String,
    @SerializedName("fecha_entrega") val fecha_entrega: String,
    @SerializedName("nota_obtenida") val nota_obtenida: Float? = null,
    @SerializedName("comentario_profesor") val comentario_profesor: String? = null,
    @SerializedName("titulo_actividad") val titulo_actividad: String? = null
)

data class CalificarEntregaRequest(
    @SerializedName("nota_obtenida") val nota_obtenida: Float,
    @SerializedName("comentario_profesor") val comentario_profesor: String? = null
)

// ==============================================================================
// --- 👤 MOLDES DE USUARIO ---
// ==============================================================================

data class UsuarioResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("nombre_usuario") val nombre_usuario: String,
    @SerializedName("nombre") val nombre: String,
    @SerializedName("apellido_paterno") val apellido_paterno: String,
    @SerializedName("apellido_materno") val apellido_materno: String,
    @SerializedName("fecha_nacimiento") val fecha_nacimiento: String,
    @SerializedName("rol") val rol: String
)