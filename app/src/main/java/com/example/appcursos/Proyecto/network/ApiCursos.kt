package com.example.appcursos.Proyecto.network

import com.example.appcursos.Proyecto.data.*
import retrofit2.Response
import retrofit2.http.*

interface ApiCursos {

    // 1. Iniciar Sesión (Ya lo tienes)
    @POST("login")
    suspend fun iniciarSesion(@Body request: LoginRequest): LoginResponse

    // 2. Ver cursos del profesor logueado
    // Coincide con: @app.get("/profesores/{maestro_id}/mis-cursos")
    @GET("profesores/{maestro_id}/mis-cursos")
    suspend fun obtenerMisCursos(@Path("maestro_id") maestroId: Int): List<CursoResponse>

    // 3. Crear un nuevo curso
    // Coincide con: @app.post("/profesores/cursos/crear")
    @POST("profesores/cursos/crear")
    suspend fun crearCurso(@Body curso: CursoCreate): CursoResponse

    // 4. Editar curso
    // Coincide con: @app.put("/profesores/cursos/editar/{curso_id}/{maestro_id}")
    @PUT("profesores/cursos/editar/{curso_id}/{maestro_id}")
    suspend fun editarCurso(
        @Path("curso_id") cursoId: Int,
        @Path("maestro_id") maestroId: Int,
        @Body curso: CursoCreate
    ): CursoResponse

    // 5. Eliminar curso
    // Coincide con: @app.delete("/profesores/cursos/eliminar/{curso_id}/{maestro_id}")
    @DELETE("profesores/cursos/eliminar/{curso_id}/{maestro_id}")
    suspend fun eliminarCurso(
        @Path("curso_id") cursoId: Int,
        @Path("maestro_id") maestroId: Int
    ): SimpleResponse
}