package com.example.smart

import android.util.Log
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class MyFirebaseMessagingService: FirebaseMessagingService() {

    companion object {
        private const val TAG = "MyFirebaseMsgService"
    }

    /** Se ejecuta cuando el dispositivo obtiene un nuevo token de registro de FCM */
    override fun onNewToken(token: String) {
        Log.d(TAG, "Nuevo token: $token")
        sendRegistrationToServer(token)
    }

    /** Se ejecuta cuando llega un mensaje desde Firebase Cloud Messaging */
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Mensaje recibido de: ${remoteMessage.from}")

        remoteMessage.notification?.let {
            val title = it.title ?: "Sin título"
            val body = it.body ?: "Sin contenido"
            Log.d(TAG, "Notificación -> Título: $title, Mensaje: $body")

            // Ejemplo: mostrar un Toast (solo si la app está en primer plano)
            Toast.makeText(applicationContext, "$title: $body", Toast.LENGTH_LONG).show()
        }
    }

    /** Enviar el token al servidor o base de datos si se necesita */
    private fun sendRegistrationToServer(token: String) {
        // Aquí puedes guardar el token en Firestore, Realtime DB o tu backend
        Log.d(TAG, "Token enviado al servidor: $token")
    }
}
