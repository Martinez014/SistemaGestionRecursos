package com.alejandromartinez.sistemagestionrecursos.repository

import com.alejandromartinez.sistemagestionrecursos.model.Recurso
import com.alejandromartinez.sistemagestionrecursos.network.RetrofitClient
import com.alejandromartinez.sistemagestionrecursos.model.Usuario

class RecursoRepository {

    suspend fun obtenerUsuarios(): List<Usuario> {
        return RetrofitClient.api.obtenerUsuarios()
    }

    suspend fun obtenerRecursos(): List<Recurso> {
        return RetrofitClient.api.obtenerRecursos()
    }

    suspend fun agregarRecurso(recurso: Recurso) {
        RetrofitClient.api.agregarRecurso(recurso)
    }

    suspend fun editarRecurso(id: String, recurso: Recurso) {
        RetrofitClient.api.editarRecurso(id, recurso)
    }

    suspend fun eliminarRecurso(id: String) {
        RetrofitClient.api.eliminarRecurso(id)
    }

    suspend fun actualizarRating(recurso: Recurso, nuevoRating: Float) {

        val promedio = (recurso.rating + nuevoRating) / 2f

        val actualizado = recurso.copy(
            rating = promedio
        )

        RetrofitClient.api.editarRecurso(recurso.id, actualizado)
    }

    suspend fun agregarUsuario(usuario: Usuario) {
        RetrofitClient.api.agregarUsuario(usuario)
    }
}