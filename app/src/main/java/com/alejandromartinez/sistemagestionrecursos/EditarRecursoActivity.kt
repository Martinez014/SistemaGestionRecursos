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

class EditarRecursoActivity : ComponentActivity() {

    private lateinit var edtTitulo: EditText
    private lateinit var edtDescripcion: EditText
    private lateinit var edtTipo: EditText
    private lateinit var btnEditar: Button

    private val repository = RecursoRepository()

    private var idRecurso = ""

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_editar_recurso)

        edtTitulo =
            findViewById(R.id.edtTituloEditar)

        edtDescripcion =
            findViewById(R.id.edtDescripcionEditar)

        edtTipo =
            findViewById(R.id.edtTipoEditar)

        btnEditar =
            findViewById(R.id.btnEditar)

        idRecurso =
            intent.getStringExtra("ID") ?: ""

        edtTitulo.setText(
            intent.getStringExtra("TITULO")
        )

        edtDescripcion.setText(
            intent.getStringExtra("DESCRIPCION")
        )

        edtTipo.setText(
            intent.getStringExtra("TIPO")
        )

        btnEditar.setOnClickListener {

            editarRecurso()
        }
    }

    private fun editarRecurso() {

        val recurso = Recurso(

            id = idRecurso,
            titulo = edtTitulo.text.toString(),
            descripcion = edtDescripcion.text.toString(),
            tipo = edtTipo.text.toString(),
            enlace = "",
            imagen = "",
            rating = 0f
        )

        CoroutineScope(Dispatchers.IO).launch {

            try {

                repository.editarRecurso(
                    idRecurso,
                    recurso
                )

                withContext(Dispatchers.Main) {

                    Toast.makeText(
                        this@EditarRecursoActivity,
                        "Recurso actualizado",
                        Toast.LENGTH_LONG
                    ).show()

                    finish()
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {

                    Toast.makeText(
                        this@EditarRecursoActivity,
                        e.message,
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}