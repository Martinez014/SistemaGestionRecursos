package com.alejandromartinez.sistemagestionrecursos

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.activity.ComponentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alejandromartinez.sistemagestionrecursos.adapter.RecursoAdapter
import com.alejandromartinez.sistemagestionrecursos.model.Recurso
import com.alejandromartinez.sistemagestionrecursos.repository.RecursoRepository
import kotlinx.coroutines.*

class MainActivity : ComponentActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecursoAdapter
    private lateinit var btnAgregar: Button
    private lateinit var btnCerrarSesion: Button
    private lateinit var btnFavoritos: Button
    private lateinit var btnFiltro: Button
    private lateinit var edtBuscar: EditText

    private lateinit var sharedPreferences: SharedPreferences
    private val repository = RecursoRepository()

    private var listaOriginal: List<Recurso> = listOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("SesionUsuario", MODE_PRIVATE)

        val nombre = sharedPreferences.getString("nombre", "") ?: ""
        val rol = sharedPreferences.getString("rol", "") ?: ""

        Toast.makeText(this, "Bienvenido $nombre - Rol: $rol", Toast.LENGTH_LONG).show()

        recyclerView = findViewById(R.id.recyclerRecursos)
        btnAgregar = findViewById(R.id.btnAgregar)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)
        btnFavoritos = findViewById(R.id.btnFavoritos)
        btnFiltro = findViewById(R.id.btnFiltro)
        edtBuscar = findViewById(R.id.edtBuscar)

        recyclerView.layoutManager = LinearLayoutManager(this)

        btnAgregar.visibility = if (rol == "docente") View.VISIBLE else View.GONE
        btnFavoritos.visibility = if (rol == "docente") View.GONE else View.VISIBLE

        btnAgregar.setOnClickListener {
            startActivity(Intent(this, AgregarRecursoActivity::class.java))
        }

        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }

        btnFavoritos.setOnClickListener {
            startActivity(Intent(this, FavoritosActivity::class.java))
        }

        edtBuscar.addTextChangedListener(object : android.text.TextWatcher {
            override fun afterTextChanged(s: android.text.Editable?) {
                filtrar(s.toString())
            }
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
        })

        btnFiltro.setOnClickListener {
            listaOriginal = listaOriginal.reversed()

            adapter = RecursoAdapter(
                listaOriginal,
                rol,
                onEliminarClick = { recurso ->
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.eliminarRecurso(recurso.id)
                        obtenerRecursos()
                    }
                },
                onRatingChange = { recurso, rating ->
                    CoroutineScope(Dispatchers.IO).launch {
                        repository.actualizarRating(recurso, rating)
                        obtenerRecursos()
                    }
                },
                onFavoritoClick = { }
            )

            recyclerView.adapter = adapter
        }

        obtenerRecursos()
    }

    override fun onResume() {
        super.onResume()
        obtenerRecursos()
    }

    private fun cerrarSesion() {
        sharedPreferences.edit().clear().apply()
        startActivity(Intent(this, LoginActivity::class.java))
        finish()
    }

    private fun obtenerRecursos() {

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val recursos = repository.obtenerRecursos()
                listaOriginal = recursos

                withContext(Dispatchers.Main) {

                    val rol = sharedPreferences.getString("rol", "") ?: ""

                    adapter = RecursoAdapter(
                        recursos,
                        rol,
                        onEliminarClick = { recurso ->

                            val builder = android.app.AlertDialog.Builder(this@MainActivity)
                            builder.setTitle("⚠️ Confirmación de eliminación")
                            builder.setMessage("¿Estás seguro de eliminar \"${recurso.titulo}\"?")

                            builder.setPositiveButton("Sí, eliminar") { dialog, _ ->

                                CoroutineScope(Dispatchers.IO).launch {
                                    repository.eliminarRecurso(recurso.id)

                                    withContext(Dispatchers.Main) {
                                        Toast.makeText(
                                            this@MainActivity,
                                            "Recurso eliminado",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    obtenerRecursos()
                                }

                                dialog.dismiss()
                            }

                            builder.setNegativeButton("Cancelar") { dialog, _ ->
                                dialog.dismiss()
                            }

                            builder.show()
                        },
                        onRatingChange = { recurso, rating ->

                            CoroutineScope(Dispatchers.IO).launch {
                                repository.actualizarRating(recurso, rating)
                                obtenerRecursos()
                            }
                        },
                        onFavoritoClick = { }
                    )

                    recyclerView.adapter = adapter
                }

            } catch (e: Exception) {
                Log.e("API_ERROR", e.message.toString())
            }
        }
    }

    private fun filtrar(texto: String) {

        val filtrados = listaOriginal.filter {
            it.titulo.contains(texto, true) ||
                    it.tipo.contains(texto, true) ||
                    it.id.contains(texto, true)
        }

        val rol = sharedPreferences.getString("rol", "") ?: ""

        recyclerView.adapter = RecursoAdapter(
            filtrados,
            rol,
            onEliminarClick = { },
            onRatingChange = { _, _ -> },
            onFavoritoClick = { } // 👈 IMPORTANTE
        )
    }
}