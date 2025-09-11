package com.example.smart

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import com.example.smart.R.layout.ayuda
import com.example.smart.R.layout.inicio_sesion
import com.example.smart.R.layout.main
import com.example.smart.R.layout.registro
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

class
Registro : AppCompatActivity() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText

    private lateinit var singInButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(registro) // tu XML se llama activity_main.xml

        // Vinculamos los elementos del layout
        emailEditText = findViewById(R.id.emailEditText)
        passwordEditText = findViewById(R.id.passwordEditText)
        singInButton = findViewById(R.id.singInButton)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_registro)) { v, insets ->
            val systemBars = insets.getInsets(androidx.core.view.WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        setup()
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
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun mostrarMenu(view: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.menu)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(ayuda)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    fun mostrarAyuda(view: View) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContentView(R.layout.ayuda)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

}