package com.alejandromartinez.sistemagestionrecursos

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.alejandromartinez.sistemagestionrecursos.model.Recurso
import com.alejandromartinez.sistemagestionrecursos.repository.RecursoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AgregarRecursoActivity : ComponentActivity() {

    private lateinit var edtTitulo: EditText
    private lateinit var edtDescripcion: EditText
    private lateinit var edtTipo: EditText
    private lateinit var btnGuardar: Button

    private val repository = RecursoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(
            R.layout.activity_agregar_recurso
        )

        edtTitulo =
            findViewById(R.id.edtTitulo)

        edtDescripcion =
            findViewById(R.id.edtDescripcion)

        edtTipo =
            findViewById(R.id.edtTipo)

        btnGuardar =
            findViewById(R.id.btnGuardar)

        btnGuardar.setOnClickListener {

            guardarRecurso()
        }
    }

    private fun guardarRecurso() {

        val recurso = Recurso(

            id = "",

            titulo =
                edtTitulo.text.toString(),

            descripcion =
                edtDescripcion.text.toString(),

            tipo =
                edtTipo.text.toString(),

            enlace =
                "",

            imagen =
                ""
        )

        CoroutineScope(Dispatchers.IO).launch {

            try {

                repository.agregarRecurso(recurso)

                withContext(Dispatchers.Main) {

                    Toast.makeText(
                        this@AgregarRecursoActivity,
                        "Recurso agregado",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {

                    Toast.makeText(
                        this@AgregarRecursoActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}