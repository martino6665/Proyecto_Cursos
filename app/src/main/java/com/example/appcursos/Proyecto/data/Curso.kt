package com.example.appcursos.Proyecto.data

import com.google.gson.annotations.SerializedName

data class Curso(
    val id: Int,
    @SerializedName("nombre_del_curso") val nombre: String,
    val descripcion: String,
    @SerializedName("id_del_profesor") val idProfesor: Int,
    @SerializedName("fecha_de_inicio") val fechaInicio: String? = null,
    @SerializedName("fecha_de_fin") val fechaFin: String? = null
)