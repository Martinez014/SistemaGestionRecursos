package com.alejandromartinez.sistemagestionrecursos.model

data class Recurso(
    val id: String,
    val titulo: String,
    val descripcion: String,
    val tipo: String,
    val enlace: String,
    val imagen: String,
    val rating: Float = 0.0f,
    val favoritos: List<String> = emptyList()
)