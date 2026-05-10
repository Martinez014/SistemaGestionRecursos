package com.alejandromartinez.sistemagestionrecursos

import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alejandromartinez.sistemagestionrecursos.adapter.RecursoAdapter
import com.alejandromartinez.sistemagestionrecursos.repository.RecursoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class FavoritosActivity : ComponentActivity() {

    private lateinit var recycler: RecyclerView
    private lateinit var prefs: SharedPreferences
    private val repo = RecursoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos)

        recycler = findViewById(R.id.recyclerRecursos)
        recycler.layoutManager = LinearLayoutManager(this)

        prefs = getSharedPreferences("SesionUsuario", MODE_PRIVATE)

        cargarFavoritos()
    }

    private fun cargarFavoritos() {

        val userId = prefs.getString("id", "") ?: ""

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val recursos = repo.obtenerRecursos()

                // 🔥 FIX REAL: leer desde SharedPreferences (NO MockAPI)
                val key = "favoritos_$userId"
                val favoritosIds = prefs.getStringSet(key, emptySet()) ?: emptySet()

                val favoritos = recursos.filter {
                    favoritosIds.contains(it.id.toString())
                }

                withContext(Dispatchers.Main) {

                    recycler.adapter = RecursoAdapter(
                        favoritos,
                        prefs.getString("rol", "") ?: "alumno",
                        onEliminarClick = { },
                        onRatingChange = { recurso, rating ->
                            CoroutineScope(Dispatchers.IO).launch {
                                repo.actualizarRating(recurso, rating)
                            }
                        },
                        onFavoritoClick = { }
                    )
                }

            } catch (e: Exception) {

                Log.e("FAVORITOS_ERROR", e.toString())
            }
        }
    }
}