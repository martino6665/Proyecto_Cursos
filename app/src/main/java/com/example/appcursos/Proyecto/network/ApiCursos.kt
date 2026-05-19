package com.example.appcursos.Proyecto.network

import com.example.appcursos.Proyecto.data.*
import retrofit2.http.*

interface ApiCursos {

    // --- ACCESO Y LOGIN ---
    @POST("login")
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse


    // --- REGISTROS (Coincide al 100% con tu Swagger UI) ---
    @POST("registro/alumno")
    suspend fun registrar_alumno(@Body alumno: AlumnoCreate): AlumnoResponse

    @POST("registro/profesor")
    suspend fun registrar_profesor(@Body profesor: ProfesorCreate): ProfesorResponse


    // --- MÓDULO PROFESORES ---
    @GET("profesores/{maestro_id}/mis-cursos")
    suspend fun ver_materias_asignadas(
        @Path("maestro_id") maestro_id: Int
    ): List<CursoResponse>

    @POST("profesores/cursos/crear")
    suspend fun crear_materia(@Body curso: CursoCreate): CursoResponse

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


    // --- MÓDULO ALUMNOS / INSCRIPCIONES ---
    @GET("alumnos/{alumno_id}/mis-cursos")
    suspend fun ver_cursos_inscritos(
        @Path("alumno_id") alumno_id: Int
    ): List<CursoResponse>

    @POST("alumnos/inscribir")
    suspend fun inscribirse(@Body inscripcion: InscripcionRequest): InscripcionResponse
}