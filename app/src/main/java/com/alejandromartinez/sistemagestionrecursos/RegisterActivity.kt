package com.alejandromartinez.sistemagestionrecursos

import android.content.Intent
import android.os.Bundle
import android.util.Patterns
import android.widget.*
import androidx.activity.ComponentActivity
import com.alejandromartinez.sistemagestionrecursos.model.Usuario
import com.alejandromartinez.sistemagestionrecursos.repository.RecursoRepository
import kotlinx.coroutines.*

class RegisterActivity : ComponentActivity() {

    private lateinit var edtNombre: EditText
    private lateinit var edtCorreo: EditText
    private lateinit var edtPassword: EditText
    private lateinit var spinnerRol: Spinner
    private lateinit var btnRegistrar: Button

    private val repository = RecursoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        edtNombre = findViewById(R.id.edtNombre)
        edtCorreo = findViewById(R.id.edtCorreo)
        edtPassword = findViewById(R.id.edtPassword)
        spinnerRol = findViewById(R.id.spinnerRol)
        btnRegistrar = findViewById(R.id.btnRegistrar)

        val roles = arrayOf("estudiante", "docente")
        spinnerRol.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, roles)

        btnRegistrar.setOnClickListener {
            registrarUsuario()
        }
    }

    private fun registrarUsuario() {

        val nombre = edtNombre.text.toString().trim()
        val correo = edtCorreo.text.toString().trim()
        val password = edtPassword.text.toString().trim()
        val rol = spinnerRol.selectedItem.toString()

        if (nombre.isEmpty() || correo.isEmpty() || password.isEmpty()) {
            toast("Completa todos los campos")
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(correo).matches()) {
            toast("Correo inválido")
            return
        }

        if (!validarPassword(password)) {
            toast("La contraseña no cumple los requisitos")
            return
        }

        CoroutineScope(Dispatchers.IO).launch {

            try {
                val usuarios = repository.obtenerUsuarios()

                val existe = usuarios.any { it.correo == correo }

                if (existe) {
                    withContext(Dispatchers.Main) {
                        toast("Este correo ya está registrado")
                    }
                    return@launch
                }

                val nuevoUsuario = Usuario(
                    id = "",
                    nombre = nombre,
                    correo = correo,
                    password = password,
                    rol = rol
                )

                repository.agregarUsuario(nuevoUsuario)

                withContext(Dispatchers.Main) {
                    toast("Usuario registrado correctamente")
                    startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                    finish()
                }

            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    toast("Error: ${e.message}")
                }
            }
        }
    }

    private fun validarPassword(password: String): Boolean {

        val regex = Regex("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*]).{12,}\$")

        return regex.matches(password)
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}