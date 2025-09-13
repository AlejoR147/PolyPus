package com.example.smart

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smart.R.layout.inicio_sesion
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class
MainActivity : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var authLayaout : ConstraintLayout
    private lateinit var singInButton: Button
    private lateinit var loginButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(inicio_sesion) // tu XML se llama activity_main.xml

        // Vinculamos los elementos del layout
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        singInButton = findViewById(R.id.singInButton)
        loginButton = findViewById(R.id.loginButton)
        authLayaout = findViewById(R.id.auth_layaout)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth_layaout)) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setup()
        sesion()
    }

    override fun onStart() {
        super.onStart()
        authLayaout.visibility = View.VISIBLE
    }


    private fun sesion() {
        //guardado de datos
        val prefs = getSharedPreferences(getString(R.string.prefs), Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)
        if (email != null && provider != null) {
            authLayaout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }
    }
    private fun setup() {
        title = "Autenticación"

        // 🔹 Botón de REGISTRO
        singInButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Registro exitoso", Toast.LENGTH_SHORT).show()
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }

        // 🔹 Botón de LOGIN
        loginButton.setOnClickListener {
            if (emailEditText.text.isNotEmpty() && passwordEditText.text.isNotEmpty()) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(
                    emailEditText.text.toString(),
                    passwordEditText.text.toString()
                ).addOnCompleteListener {
                    if (it.isSuccessful) {
                        Toast.makeText(this, "Sesion iniciada", Toast.LENGTH_SHORT).show()
                        showHome(it.result?.user?.email ?: "", ProviderType.BASIC)
                    } else {
                        showAlert()
                    }
                }
            }
        }
    }

    private fun showAlert() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
        builder.setMessage("Se ha producido un error autenticando al usuario")
        builder.setPositiveButton("Aceptar", null)
        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun showHome(email: String, provider: ProviderType) {
        val homeIntent = Intent(this, HomeActivity::class.java).apply {
            putExtra("email", email)
            putExtra("provider", provider.name)
        }
        startActivity(homeIntent)
        finish() // <- evita que el usuario regrese al login
    }

    fun showRegistro(view: View) {
        val intent = Intent(this, Registro::class.java)
        startActivity(intent)
        finish() // Cierra la actividad actual
    }


    fun mostrarRegistro(view: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.registro)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_registro)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun mostrarInicio(view: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.inicio_sesion)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth_layaout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun mostrarMenu(view: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth_layaout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun mostrarAyuda(view: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.ayuda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.auth_layaout)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}