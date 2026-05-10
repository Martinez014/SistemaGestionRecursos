package com.alejandromartinez.sistemagestionrecursos.network

import com.alejandromartinez.sistemagestionrecursos.model.Recurso
import com.alejandromartinez.sistemagestionrecursos.model.Usuario
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {

    @GET("recursos")
    suspend fun obtenerRecursos(): List<Recurso>

    @GET("usuarios")
    suspend fun obtenerUsuarios(): List<Usuario>

    @POST("recursos")
    suspend fun agregarRecurso(
        @Body recurso: Recurso
    ): Recurso

    @PUT("recursos/{id}")
    suspend fun editarRecurso(
        @Path("id") id: String,
        @Body recurso: Recurso
    ): Recurso

    @DELETE("recursos/{id}")
    suspend fun eliminarRecurso(
        @Path("id") id: String
    )

    @POST("usuarios")
    suspend fun agregarUsuario(
        @Body usuario: Usuario
    ): Usuario
}