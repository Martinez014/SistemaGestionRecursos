package com.alejandromartinez.sistemagestionrecursos.model

data class Usuario(
    val id: String,
    val nombre: String,
    val correo: String,
    val password: String,
    val rol: String
)