package com.example.smart.data.repository

import com.example.smart.data.model.Transaction
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.tasks.await
import com.google.firebase.firestore.ktx.firestore
import kotlinx.coroutines.tasks.await

class FirestoreRepository {

    private val db = Firebase.firestore

    // Referencia al documento del usuario
    private fun userRef(uid: String) =
        db.collection("UserID").document(uid)

    // Subcolección de transacciones del usuario
    private fun transactionsRef(uid: String) =
        userRef(uid).collection("transactions")

    /** 🔹 Agregar transacción y actualizar totales */
    suspend fun addTransaction(userId: String, transaction: Transaction): String {
        val ref = transactionsRef(userId).add(transaction).await()
        updateUserTotals(userId)
        return ref.id
    }

    /** 🔹 Editar transacción existente */
    suspend fun updateTransaction(userId: String, transactionId: String, transaction: Transaction) {
        transactionsRef(userId).document(transactionId).set(transaction).await()
        updateUserTotals(userId)
    }

    /** 🔹 Eliminar transacción */
    suspend fun deleteTransaction(userId: String, transactionId: String) {
        transactionsRef(userId).document(transactionId).delete().await()
        updateUserTotals(userId)
    }

    /** 🔹 Obtener lista de transacciones */
    suspend fun getTransactions(userId: String): List<Transaction> {
        val snap = transactionsRef(userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .get()
            .await()
        return snap.toObjects(Transaction::class.java)
    }

    /** 🔹 Escucha en tiempo real (para actualizaciones automáticas) */
    fun listenTransactions(userId: String, onUpdate: (List<Transaction>) -> Unit): ListenerRegistration {
        return transactionsRef(userId)
            .orderBy("date", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, error ->
                if (error != null) return@addSnapshotListener
                val list = snap?.toObjects(Transaction::class.java) ?: emptyList()
                onUpdate(list)
            }
    }

    /** 🔹 Recalcular totales del usuario (balance, ingresos, gastos) */
    private suspend fun updateUserTotals(userId: String) {
        val snap = transactionsRef(userId).get().await()
        val list = snap.toObjects(Transaction::class.java)

        val totalIncome = list.filter { it.type == "income" }.sumOf { it.amount }
        val totalExpenses = list.filter { it.type == "gasto" }.sumOf { it.amount }
        val balance = totalIncome - totalExpenses

        userRef(userId).update(
            mapOf(
                "totalIncome" to totalIncome,
                "totalExpenses" to totalExpenses,
                "balance" to balance
            )
        ).await()
    }
}