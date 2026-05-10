package com.alejandromartinez.sistemagestionrecursos

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.alejandromartinez.sistemagestionrecursos.repository.RecursoRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginActivity : ComponentActivity() {

    private lateinit var edtCorreo: EditText
    private lateinit var edtPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var txtCrearCuenta: TextView

    private lateinit var sharedPreferences: SharedPreferences

    private val repository = RecursoRepository()

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        sharedPreferences =
            getSharedPreferences("SesionUsuario", MODE_PRIVATE)

        val sesionActiva =
            sharedPreferences.getBoolean("sesion_activa", false)

        if (sesionActiva) {

            startActivity(
                Intent(this, MainActivity::class.java)
            )

            finish()
        }

        setContentView(R.layout.activity_login)

        edtCorreo = findViewById(R.id.edtCorreo)
        edtPassword = findViewById(R.id.edtPassword)
        btnLogin = findViewById(R.id.btnLogin)

        txtCrearCuenta = findViewById(R.id.tvGoToRegister)

        txtCrearCuenta.setOnClickListener {

            val intent = Intent(
                this,
                RegisterActivity::class.java
            )
            startActivity(intent)
        }

        btnLogin.setOnClickListener {
            login()
        }
    }

    private fun login() {

        val correo = edtCorreo.text.toString()
        val password = edtPassword.text.toString()

        CoroutineScope(Dispatchers.IO).launch {

            try {

                val usuarios = repository.obtenerUsuarios()

                val usuarioEncontrado = usuarios.find {
                    it.correo == correo &&
                            it.password == password
                }

                withContext(Dispatchers.Main) {

                    if (usuarioEncontrado != null) {

                        sharedPreferences.edit()
                            .putBoolean("sesion_activa", true)
                            .putString("id", usuarioEncontrado.id)
                            .putString("nombre", usuarioEncontrado.nombre)
                            .putString("correo", usuarioEncontrado.correo)
                            .putString("rol", usuarioEncontrado.rol)
                            .apply()

                        Toast.makeText(
                            this@LoginActivity,
                            "Bienvenido ${usuarioEncontrado.nombre}",
                            Toast.LENGTH_LONG
                        ).show()

                        startActivity(
                            Intent(
                                this@LoginActivity,
                                MainActivity::class.java
                            )
                        )

                        finish()

                    } else {

                        Toast.makeText(
                            this@LoginActivity,
                            "Credenciales incorrectas",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

            } catch (e: Exception) {

                withContext(Dispatchers.Main) {

                    Toast.makeText(
                        this@LoginActivity,
                        "Error: ${e.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}