package com.example.appcursos.Proyecto.network

import com.example.appcursos.Proyecto.data.*
import retrofit2.http.*

interface ApiCursos {

    // ==============================================================================
    // --- 🔑 ACCESO, LOGIN Y REGISTROS ---
    // ==============================================================================

    @POST("login")
    suspend fun iniciarSesion(
        @Body request: LoginRequest
    ): LoginResponse

    @POST("registro/alumno")
    suspend fun registrar_alumno(
        @Body alumno: AlumnoCreate
    ): AlumnoResponse

    @POST("registro/profesor")
    suspend fun registrar_profesor(
        @Body profesor: ProfesorCreate
    ): ProfesorResponse


    // ==============================================================================
    // --- 👨‍🏫 MÓDULO PROFESORES: GESTIÓN DE MATERIAS ---
    // ==============================================================================

    @GET("profesores/alumnos")
    suspend fun obtenerListaDeAlumnos(): List<AlumnoResponse>

    @GET("profesores/{maestro_id}/mis-cursos")
    suspend fun ver_materias_asignadas(
        @Path("maestro_id") maestro_id: Int
    ): List<CursoResponse>

    @POST("profesores/cursos/crear")
    suspend fun crear_materia(
        @Body curso: CursoCreate
    ): CursoResponse

    @PUT("profesores/cursos/actualizar/{curso_id}/{maestro_id}")
    suspend fun actualizar_materia(
        @Path("curso_id") curso_id: Int,
        @Path("maestro_id") maestro_id: Int,
        @Body curso_data: CursoCreate
    ): CursoResponse

    @DELETE("profesores/cursos/eliminar/{curso_id}/{maestro_id}")
    suspend fun eliminar_materia(
        @Path("curso_id") curso_id: Int,
        @Path("maestro_id") maestro_id: Int
    ): SimpleResponse

    @PUT("profesores/calificar/{alumno_id}/{curso_id}")
    suspend fun calificar_alumno(
        @Path("alumno_id") alumno_id: Int,
        @Path("curso_id") curso_id: Int,
        @Body calificacion: CalificacionUpdate
    ): SimpleResponse


    // ==============================================================================
    // --- 🎓 MÓDULO ALUMNOS: INSCRIPCIONES ---
    // ==============================================================================

    @GET("alumnos/cursos/buscar")
    suspend fun buscar_todos_los_cursos(
        @Query("query") query: String = ""
    ): List<CursoResponse>

    @GET("alumnos/{alumno_id}/mis-cursos")
    suspend fun ver_cursos_inscritos(
        @Path("alumno_id") alumno_id: Int
    ): List<CursoResponse>

    @POST("alumnos/inscribir")
    suspend fun inscribirse(
        @Body inscripcion: InscripcionCreate
    ): InscripcionResponse


    // ==============================================================================
    // --- 📝 BLOQUE: GESTIÓN DE ACTIVIDADES, ENTREGAS Y CALENDARIOS ---
    // ==============================================================================

    // 1. Profesor: Asignar nueva tarea
    @POST("profesores/cursos/{curso_id}/actividades")
    suspend fun asignar_actividad_a_materia(
        @Path("curso_id") cursoId: Int,
        @Body actividad: ActividadCreate
    ): ActividadResponse

    // 2. Profesor: Listar alumnos inscritos (ASEGÚRATE que tu backend use exactamente este path)
    @GET("profesores/cursos/{curso_id}/alumnos-inscritos")
    suspend fun ver_alumnos_activos_en_curso(
        @Path("curso_id") cursoId: Int
    ): List<UsuarioResponse>

    // 3. Profesor: Calificar entrega individual
    @PUT("profesores/entregas/{entrega_id}/calificar")
    suspend fun evaluar_entrega_alumno(
        @Path("entrega_id") entregaId: Int,
        @Body evaluacion: CalificarEntregaRequest
    ): EntregaResponse

    // 4. Alumno: Enviar tarea
    @POST("alumnos/actividades/{actividad_id}/entregar/{alumno_id}")
    suspend fun entregar_actividad(
        @Path("actividad_id") actividadId: Int,
        @Path("alumno_id") alumnoId: Int,
        @Body entrega: EntregaCreate
    ): EntregaResponse

    // --- NUEVAS RUTAS DE CALENDARIO DINÁMICO ---

    // 5. Alumno: Obtener agenda real (tareas enviadas y recibidas)
    @GET("alumnos/{alumno_id}/agenda")
    suspend fun obtenerAgendaRealAlumno(
        @Path("alumno_id") alumnoId: Int
    ): List<EntregaResponse>

    // 6. Profesor: Obtener tareas pendientes por revisar
    @GET("profesores/{maestro_id}/pendientes")
    suspend fun obtenerPendientesRealMaestro(
        @Path("maestro_id") maestroId: Int
    ): List<EntregaResponse>
    @GET("profesores/cursos/{curso_id}/actividades")
    suspend fun obtenerActividadesPorCurso(
        @Path("curso_id") cursoId: Int
    ): List<ActividadResponse>
}